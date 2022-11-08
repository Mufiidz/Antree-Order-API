package id.my.mufidz.services

import id.my.mufidz.dao.UserDao
import id.my.mufidz.model.*
import id.my.mufidz.model.dto.LoginDTO
import id.my.mufidz.model.dto.RegisterDTO
import id.my.mufidz.plugins.dbQuery
import id.my.mufidz.response.WebResponse
import id.my.mufidz.routes.IdNotFoundException
import id.my.mufidz.security.hashing.HashingService
import id.my.mufidz.security.token.TokenService
import id.my.mufidz.utils.generateId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.sessions.*

interface UserAuthService {
    suspend fun register(request: RegisterDTO): WebResponse<String>
    suspend fun login(request: LoginDTO, application: Application): WebResponse<User>
    suspend fun deleteUser(userId: String): WebResponse<String>
    fun logOut(session: CurrentSession) : WebResponse<String>
}

class UserAuthServiceImpl(
    private val hashingService: HashingService,
    private val userDao: UserDao,
    private val tokenService: TokenService
) : UserAuthService {

    override suspend fun register(request: RegisterDTO): WebResponse<String> {
        val newUsername = checkUsername(request.username)
        val saltedHash = hashingService.generatePasswordHashed(request.password)
        Register(generateId(), request.name, newUsername, saltedHash).also {
            userDao.addUser(it)
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Success", "Berhasil menambahkan ${request.name}"
        )
    }

    override suspend fun login(request: LoginDTO, application: Application): WebResponse<User> {
        val username = request.username.replace("\\s".toRegex(), "").lowercase()
        val userEntity =
            userDao.getUserByUsername(username) ?: throw BadRequestException("Username '$username' doesn't exist")
        val isValidPassword = hashingService.verify(
            request.password,
            SaltedHash(userEntity.password, userEntity.salt)
        )
        if (!isValidPassword) {
            throw BadRequestException("Incorrect password")
        }
        val token = tokenService.generate(
            getTokenConfig(application),
            TokenClaim("userId", userEntity.id.value)
        )
        val user = userEntity.toUser(token)

        return WebResponse(
            HttpStatusCode.OK.value, "Successfully logged in", user
        )
    }

    private fun getTokenConfig(application: Application): TokenConfig = TokenConfig(
        application.environment.config.property("jwt.issuer").getString(),
        application.environment.config.property("jwt.audience").getString(),
        System.getenv("JWT_SECRET")
    )

    override suspend fun deleteUser(userId: String): WebResponse<String> {
        when {
            userId.isEmpty() -> throw MissingRequestParameterException("UserId")
            !userDao.checkUserById(userId) -> throw IdNotFoundException("UserId")
        }
        userDao.deleteUser(userId)
        return WebResponse(
            HttpStatusCode.OK.value, "Success", "Berhasil menghapus $userId"
        )
    }

    override fun logOut(session: CurrentSession): WebResponse<String> {
        session.clear<UserSession>()
        return WebResponse(
            HttpStatusCode.OK.value, "Success", "You've been logged out"
        )
    }

    private suspend fun checkUsername(username: String): String {
        val newUsername = username.replace("\\s".toRegex(), "").lowercase()
        val usernameValidation = "^(?![_.])(?!.*[_.]{2})[a-z0-9._]+(?<![.])$".toRegex()
        if (usernameValidation.containsMatchIn(newUsername)) {
            dbQuery {
                userDao.getUserByUsername(newUsername).takeIf { it != null }?.let {
                    throw BadRequestException("Username '$newUsername' is already taken")
                }
            }
        } else {
            throw BadRequestException("Username '$username' not allowed")
        }
        return newUsername
    }
}