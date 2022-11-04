package id.my.mufidz.services

import id.my.mufidz.dao.MerchantDao
import id.my.mufidz.model.*
import id.my.mufidz.model.dto.LoginDTO
import id.my.mufidz.model.dto.RegisterMerchantDTO
import id.my.mufidz.plugins.dbQuery
import id.my.mufidz.response.WebResponse
import id.my.mufidz.security.hashing.HashingService
import id.my.mufidz.security.token.TokenService
import id.my.mufidz.utils.generateId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.sessions.*
import java.util.*

interface MerchantAuthService {
    suspend fun register(request: RegisterMerchantDTO): WebResponse<String>
    suspend fun login(request: LoginDTO, application: Application): WebResponse<Merchant>
    suspend fun deleteUser(merchantId: String): WebResponse<String>
    fun logOut(session: CurrentSession): WebResponse<String>
}

class MerchantAuthServiceImpl(
    private val hashingService: HashingService,
    private val merchantDao: MerchantDao,
    private val tokenService: TokenService
) : MerchantAuthService {

    override suspend fun register(request: RegisterMerchantDTO): WebResponse<String> {
        dbQuery {
            val newUsername = checkUsername(request.username)
            val saltedHash = hashingService.generatePasswordHashed(request.password)
            RegisterMerchant(
                UUID.randomUUID().generateId(),
                request.name,
                request.description,
                newUsername,
                saltedHash
            ).also {
                merchantDao.addMerchant(it)
            }
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Success", "Berhasil menambahkan ${request.name}"
        )
    }

    override suspend fun login(request: LoginDTO, application: Application): WebResponse<Merchant> {
        val username = request.username.replace("\\s".toRegex(), "").lowercase()
        var merchant = Merchant()
        dbQuery {
            val userEntity =
                merchantDao.getMerchantByUsername(username)
                    ?: throw BadRequestException("Username '$username' doesn't exist")
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
            merchant = userEntity.toMerchant(token)
        }

        return WebResponse(
            HttpStatusCode.OK.value, "Successfully logged in", merchant
        )
    }

    private fun getTokenConfig(application: Application): TokenConfig = TokenConfig(
        application.environment.config.property("jwt.issuer").getString(),
        application.environment.config.property("jwt.audience").getString(),
        System.getenv("JWT_SECRET")
    )

    override suspend fun deleteUser(merchantId: String): WebResponse<String> {
        if (merchantId.isEmpty()) {
            throw BadRequestException("MerchantId is empty")
        } else {
            dbQuery {
                merchantDao.deleteMerchant(merchantId)
            }
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Success", "Berhasil menghapus $merchantId"
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
                merchantDao.getMerchantByUsername(newUsername).takeIf { it != null }?.let {
                    throw BadRequestException("Username '$newUsername' is already taken")
                }
            }
        } else {
            throw BadRequestException("Username '$username' not allowed")
        }
        return newUsername
    }
}