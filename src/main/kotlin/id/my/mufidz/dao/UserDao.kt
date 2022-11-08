package id.my.mufidz.dao

import id.my.mufidz.model.*
import id.my.mufidz.model.entity.UserEntity
import id.my.mufidz.model.table.AntrianTable
import id.my.mufidz.model.table.UserTable
import id.my.mufidz.plugins.dbQuery

interface UserDao {
    suspend fun addUser(register: Register): UserEntity
    suspend fun checkUserById(userId: String): Boolean
    suspend fun getUserByUsername(username: String): UserEntity?
    suspend fun deleteUser(userId: String)
    suspend fun listAntrianByUserId(
        userId: String,
        queryDate: QueryDate?,
        statusCode: Int?,
        page: Int,
        size: Int
    ): Paginate<Antrian>
}

class UserDaoImpl(private val antrianDao: AntrianDao) : UserDao {
    override suspend fun addUser(register: Register): UserEntity = dbQuery {
        UserEntity.new(register.id) { fromInsert(register) }
    }

    override suspend fun checkUserById(userId: String): Boolean = dbQuery {
        UserEntity.findById(userId) != null
    }

    override suspend fun getUserByUsername(username: String) = dbQuery {
        UserEntity.find {
            UserTable.username eq username
        }.firstOrNull()
    }

    override suspend fun deleteUser(userId: String) = dbQuery {
        UserEntity[userId].delete()
    }

    override suspend fun listAntrianByUserId(
        userId: String,
        queryDate: QueryDate?,
        statusCode: Int?,
        page: Int,
        size: Int
    ): Paginate<Antrian> {
        val antreeQuery = AntreeQuery(
            userId,
            AntrianTable.userId,
            statusCode, queryDate, page, size
        )
        return antrianDao.listAntrianByQuery(antreeQuery)
    }
}

