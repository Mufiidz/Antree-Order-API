package id.my.mufidz.services

import id.my.mufidz.dao.MerchantDao
import id.my.mufidz.dao.ProductDao
import id.my.mufidz.model.Antrian
import id.my.mufidz.model.Merchant
import id.my.mufidz.model.Page
import id.my.mufidz.model.QueryDate
import id.my.mufidz.model.dto.MerchantDTO
import id.my.mufidz.model.dto.ProductDTO
import id.my.mufidz.response.PaginateWebResponse
import id.my.mufidz.response.WebResponse
import id.my.mufidz.routes.IdNotFoundException
import io.ktor.http.*
import io.ktor.server.plugins.*

interface MerchantService {
    suspend fun getAllMerchant(page: Int, size: Int): PaginateWebResponse<List<MerchantDTO>>
    suspend fun getAllProductByMerchantId(
        merchantId: String, page: Int, size: Int
    ): PaginateWebResponse<List<ProductDTO>>

    suspend fun getDetailMerchant(merchantId: String): WebResponse<Merchant>
    suspend fun getAllAntrianByIdMerchant(
        merchantId: String,
        queryDate: QueryDate?,
        statusCode: Int?,
        page: Int,
        size: Int
    ): PaginateWebResponse<List<Antrian>>

    suspend fun updateisOpenMerchant(merchantId: String, isOpen: Boolean = false) : WebResponse<Merchant>
}

class MerchantServicesImpl(
    private val merchantDao: MerchantDao, private val productDao: ProductDao
) : MerchantService {
    override suspend fun getAllMerchant(page: Int, size: Int): PaginateWebResponse<List<MerchantDTO>> {
        var listMerchant = emptyList<MerchantDTO>()
        var total = 0
        val newPage = if (page <= 0) 0 else page
        val newSize = if (size <= 5) 5 else size
        merchantDao.getAllMerchant(newPage, newSize).also {
            listMerchant = it.list.map { merchantEntity ->
                merchantEntity.toMerchantDTO()
            }
            total = it.total
        }
        val pageInfo = Page(size, total, (page + 1))
        pageInfo.totalPage = pageInfo.safeTotalPage
        return PaginateWebResponse(
            HttpStatusCode.OK.value, "Success", listMerchant, pageInfo
        )
    }

    override suspend fun getAllProductByMerchantId(
        merchantId: String, page: Int, size: Int
    ): PaginateWebResponse<List<ProductDTO>> {
        val newPage = if (page <= 0) 0 else page
        val newSize = if (size <= 5) 5 else size
        var listProduct: List<ProductDTO>
        var total: Int
        when {
            merchantId.isEmpty() -> throw MissingRequestParameterException("MerchantId")
            !merchantDao.checkMerchantById(merchantId) -> throw IdNotFoundException("MerchantId")
        }
        productDao.getListProductByMerchantId(merchantId, newPage, newSize).also {
            listProduct = it.list
            total = it.total
        }
        val pageInfo = Page(size, total, (page + 1))
        pageInfo.totalPage = pageInfo.safeTotalPage
        return PaginateWebResponse(
            HttpStatusCode.OK.value, "Success", listProduct, pageInfo
        )
    }

    override suspend fun getDetailMerchant(merchantId: String): WebResponse<Merchant> {
        when {
            merchantId.isEmpty() -> throw MissingRequestParameterException("MerchantId")
            !merchantDao.checkMerchantById(merchantId) -> throw IdNotFoundException("MerchantId")
        }
        val merchant = merchantDao.getMerchantById(merchantId)
        return WebResponse(
            HttpStatusCode.OK.value, "Success", merchant
        )
    }

    override suspend fun getAllAntrianByIdMerchant(
        merchantId: String,
        queryDate: QueryDate?,
        statusCode: Int?,
        page: Int,
        size: Int
    ): PaginateWebResponse<List<Antrian>> {
        when {
            merchantId.isEmpty() -> throw MissingRequestParameterException("MerchantId")
            !merchantDao.checkMerchantById(merchantId) -> throw IdNotFoundException("MerchantId")
        }
        val newPage = if (page <= 0) 0 else page
        val newSize = if (size <= 5) 5 else size
        val paginate = merchantDao.listAntrianByMerchantId(merchantId, queryDate, statusCode, newPage, newSize)
        val pageInfo = Page(size, paginate.total, (page + 1))
        pageInfo.totalPage = pageInfo.safeTotalPage
        return PaginateWebResponse(
            HttpStatusCode.OK.value, "Success", paginate.list, pageInfo
        )
    }

    override suspend fun updateisOpenMerchant(merchantId: String, isOpen: Boolean): WebResponse<Merchant> {
        when {
            merchantId.isEmpty() -> throw MissingRequestParameterException("MerchantId")
            !merchantDao.checkMerchantById(merchantId) -> throw IdNotFoundException("MerchantId")
        }
        val status = if (isOpen) "Open" else "Close"
        return WebResponse(
            HttpStatusCode.OK.value, "Merchant is $status", merchantDao.updateOpenMerchant(merchantId, isOpen)
        )
    }
}