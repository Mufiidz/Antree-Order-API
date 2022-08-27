package id.my.mufidz.dao

import id.my.mufidz.model.Register
import id.my.mufidz.model.entity.UserEntity
import id.my.mufidz.model.table.UserTable
import io.ktor.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.transaction

interface UserDao {
    suspend fun addUser(register: Register): UserEntity
    suspend fun getUserByUsername(username: String) : UserEntity?
    suspend fun deleteUser(userId: String)
}

class UserDaoImpl : UserDao {
    override suspend fun addUser(register: Register): UserEntity = transaction {
        UserEntity.new(register.id) {
            name = register.name
            username = register.username.toLowerCasePreservingASCIIRules()
            password = register.saltedHash.hash
            salt = register.saltedHash.salt
            createdAt = Clock.System.now().toString()
        }
    }

    override suspend fun getUserByUsername(username: String) = transaction {
        UserEntity.find {
            UserTable.username eq username
        }.firstOrNull()
    }

    override suspend fun deleteUser(userId: String) = transaction {
        UserEntity[userId].delete()
    }
}

