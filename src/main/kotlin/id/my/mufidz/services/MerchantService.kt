package id.my.mufidz.services

import id.my.mufidz.dao.MerchantDao
import id.my.mufidz.model.Page
import id.my.mufidz.model.dto.MerchantDTO
import id.my.mufidz.plugins.dbQuery
import id.my.mufidz.response.PaginateWebResponse
import io.ktor.http.*

interface MerchantService {
    suspend fun getAllMerchant(page: Int, size: Int): PaginateWebResponse<List<MerchantDTO>>
}

class MerchantServicesImpl(
    private val merchantDao: MerchantDao
) : MerchantService {
    override suspend fun getAllMerchant(page: Int, size: Int): PaginateWebResponse<List<MerchantDTO>> {
        var listMerchant = emptyList<MerchantDTO>()
        var total = 0
        dbQuery {
            merchantDao.getAllMerchant(page, size).also {
                listMerchant = it.list.map { merchantEntity -> merchantEntity.toMerchantDTO() }
                total = it.total
            }
        }
        val pageInfo = Page(size, total, (page + 1))
        pageInfo.totalPage = pageInfo.safeTotalPage
        return PaginateWebResponse(
            HttpStatusCode.OK.value, "Success", listMerchant, pageInfo
        )
    }
}