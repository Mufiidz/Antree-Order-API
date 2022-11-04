package id.my.mufidz.dao

import id.my.mufidz.model.Paginate
import id.my.mufidz.model.RegisterMerchant
import id.my.mufidz.model.dto.MerchantDTO
import id.my.mufidz.model.entity.MerchantEntity
import id.my.mufidz.model.table.MerchantTable
import id.my.mufidz.utils.localeDateNow
import io.ktor.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.transaction

interface MerchantDao {
    suspend fun addMerchant(register: RegisterMerchant): MerchantEntity
    suspend fun getMerchantById(merchantId: String): MerchantDTO
    suspend fun checkMerchantById(merchantId: String) : Boolean
    suspend fun getMerchantByUsername(username: String): MerchantEntity?
    suspend fun getAllMerchant(page: Int, size: Int): Paginate<MerchantEntity>
    suspend fun deleteMerchant(id: String)
}

class MerchantDaoImpl : MerchantDao {
    override suspend fun addMerchant(register: RegisterMerchant): MerchantEntity = transaction {
        MerchantEntity.new(register.id) {
            name = register.name
            username = register.username.toLowerCasePreservingASCIIRules()
            description = register.desc
            password = register.saltedHash.hash
            salt = register.saltedHash.salt
            createdAt = Clock.System.localeDateNow()
        }
    }

    override suspend fun getMerchantById(merchantId: String) : MerchantDTO = transaction {
        MerchantEntity[merchantId].toMerchantDTO()
    }

    override suspend fun checkMerchantById(merchantId: String): Boolean = transaction {
        MerchantEntity.findById(merchantId) != null
    }

    override suspend fun getMerchantByUsername(username: String) = transaction {
        MerchantEntity.find {
            MerchantTable.username eq username
        }.firstOrNull()
    }

    override suspend fun getAllMerchant(page: Int, size: Int) = transaction {
        val newPage = if (page <= 0) 0 else page
        val newSize = if (size <= 5) 5 else size
        val total = MerchantEntity.all().count().toInt()
        val skip = newSize * newPage
        val list = MerchantEntity.all().limit(size, skip.toLong()).toList()
        Paginate(total, list)
    }

    override suspend fun deleteMerchant(id: String) = transaction {
        MerchantEntity[id].delete()
    }
}

