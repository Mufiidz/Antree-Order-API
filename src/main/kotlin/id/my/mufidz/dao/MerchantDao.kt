package id.my.mufidz.dao

import id.my.mufidz.model.*
import id.my.mufidz.model.entity.MerchantEntity
import id.my.mufidz.model.table.AntrianTable
import id.my.mufidz.model.table.MerchantTable
import id.my.mufidz.plugins.dbQuery

interface MerchantDao {
    suspend fun addMerchant(register: RegisterMerchant): MerchantEntity
    suspend fun getMerchantById(merchantId: String): Merchant
    suspend fun checkMerchantById(merchantId: String): Boolean
    suspend fun getMerchantByUsername(username: String): MerchantEntity?
    suspend fun getAllMerchant(page: Int, size: Int): Paginate<MerchantEntity>
    suspend fun deleteMerchant(id: String)
    suspend fun listAntrianByMerchantId(
        merchantId: String, queryDate: QueryDate?, statusCode: Int?, page: Int, size: Int
    ): Paginate<Antrian>

    suspend fun updateOpenMerchant(merchantId: String, isOpen: Boolean = false): Merchant
    suspend fun checkStatusOpenMerchant(merchantId: String): Boolean
}

class MerchantDaoImpl(private val antrianDao: AntrianDao) : MerchantDao {
    override suspend fun addMerchant(register: RegisterMerchant): MerchantEntity = dbQuery {
        MerchantEntity.new(register.id) { fromRegisterMerchant(register) }
    }

    override suspend fun getMerchantById(merchantId: String): Merchant = dbQuery {
        MerchantEntity[merchantId].toMerchant()
    }

    override suspend fun checkMerchantById(merchantId: String): Boolean = dbQuery {
        MerchantEntity.findById(merchantId) != null
    }

    override suspend fun getMerchantByUsername(username: String): MerchantEntity? = dbQuery {
        MerchantEntity.find {
            MerchantTable.username eq username
        }.firstOrNull()
    }

    override suspend fun getAllMerchant(page: Int, size: Int): Paginate<MerchantEntity> = dbQuery {
        val total = MerchantEntity.all().count().toInt()
        val skip = size * page
        val list = MerchantEntity.all().limit(size, skip.toLong()).toList()
        Paginate(total, list)
    }

    override suspend fun deleteMerchant(id: String) = dbQuery {
        MerchantEntity[id].delete()
    }

    override suspend fun listAntrianByMerchantId(
        merchantId: String, queryDate: QueryDate?, statusCode: Int?, page: Int, size: Int
    ): Paginate<Antrian> {
        val antreeQuery = AntreeQuery(
            merchantId, AntrianTable.merchantId, statusCode, queryDate, page, size
        )
        return antrianDao.listAntrianByQuery(antreeQuery)
    }

    override suspend fun updateOpenMerchant(merchantId: String, isOpen: Boolean): Merchant = dbQuery {
        MerchantEntity[merchantId].also { it.isOpen = isOpen }.toMerchant()
    }

    override suspend fun checkStatusOpenMerchant(merchantId: String): Boolean =
        dbQuery { MerchantEntity[merchantId].isOpen }
}

