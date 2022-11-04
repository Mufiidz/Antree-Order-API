package id.my.mufidz.services

import id.my.mufidz.dao.MerchantDao
import id.my.mufidz.dao.ProductDao
import id.my.mufidz.model.Page
import id.my.mufidz.model.dto.MerchantDTO
import id.my.mufidz.model.dto.ProductDTO
import id.my.mufidz.plugins.dbQuery
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
    suspend fun getDetailMerchant(merchantId: String): WebResponse<MerchantDTO>
}

class MerchantServicesImpl(
    private val merchantDao: MerchantDao,
    private val productDao: ProductDao
) : MerchantService {
    override suspend fun getAllMerchant(page: Int, size: Int): PaginateWebResponse<List<MerchantDTO>> {
        var listMerchant = emptyList<MerchantDTO>()
        var total = 0
        dbQuery {
            merchantDao.getAllMerchant(page, size).also {
                listMerchant = it.list.map { merchantEntity ->
                    merchantEntity.toMerchantDTO()
                }
                total = it.total
            }
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
        var listProduct = emptyList<ProductDTO>()
        var total = 0
        when {
            merchantId.isEmpty() -> throw BadRequestException("MerchantId is empty")
            !merchantDao.checkMerchantById(merchantId) -> throw IdNotFoundException("MerchantId $merchantId tidak ditemukan")
            else -> {
                dbQuery {
                    productDao.getListProductByMerchantId(merchantId, page, size).also {
                        listProduct = it.list
                        total = it.total
                    }
                }
            }
        }
        val pageInfo = Page(size, total, (page + 1))
        pageInfo.totalPage = pageInfo.safeTotalPage
        return PaginateWebResponse(
            HttpStatusCode.OK.value, "Success", listProduct, pageInfo
        )
    }

    override suspend fun getDetailMerchant(merchantId: String): WebResponse<MerchantDTO> {
        var merchantDTO = MerchantDTO()
        when {
            merchantId.isEmpty() -> throw BadRequestException("MerchantId is empty")
            !merchantDao.checkMerchantById(merchantId) -> throw IdNotFoundException("MerchantId $merchantId tidak ditemukan")
            else -> {
                dbQuery {
                    merchantDTO = merchantDao.getMerchantById(merchantId)
                }
            }
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Success", merchantDTO
        )
    }
}