package id.my.mufidz.services

import id.my.mufidz.dao.UserDao
import id.my.mufidz.model.Antrian
import id.my.mufidz.model.Page
import id.my.mufidz.model.QueryDate
import id.my.mufidz.response.PaginateWebResponse
import id.my.mufidz.routes.IdNotFoundException
import io.ktor.http.*
import io.ktor.server.plugins.*

interface UserServices {
    suspend fun getListAntrianById(
        userId: String, queryDate: QueryDate?, statusCode: Int?, page: Int, size: Int
    ): PaginateWebResponse<List<Antrian>>
}

class UserServicesImpl(private val userDao: UserDao) : UserServices {

    override suspend fun getListAntrianById(
        userId: String, queryDate: QueryDate?, statusCode: Int?, page: Int, size: Int
    ): PaginateWebResponse<List<Antrian>> {
        when {
            userId.isEmpty() -> throw MissingRequestParameterException("UserId")
            !userDao.checkUserById(userId) -> throw IdNotFoundException("UserId")
        }
        val newPage = if (page <= 0) 0 else page
        val newSize = if (size <= 5) 5 else size
        val paginate = userDao.listAntrianByUserId(userId, queryDate, statusCode, newPage, newSize)
        val pageInfo = Page(size, paginate.total, (page + 1))
        pageInfo.totalPage = pageInfo.safeTotalPage
        return PaginateWebResponse(
            HttpStatusCode.OK.value, "Success", paginate.list, pageInfo
        )
    }

}