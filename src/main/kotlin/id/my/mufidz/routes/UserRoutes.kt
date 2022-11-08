package id.my.mufidz.routes

import id.my.mufidz.model.QueryDate
import id.my.mufidz.services.UserServices
import id.my.mufidz.utils.params
import id.my.mufidz.utils.queryParams
import id.my.mufidz.utils.toDigitOnly
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDate

fun Route.usersRoutes() {
    val userServices by inject<UserServices>()

    route("user") {
        get("antree/{userId}") {
            val id = call.params("userId")
            val statusCode = call.queryParams("statusId").filter { it.isDigit() }.toIntOrNull()
            var page = call.queryParams("page", "1").toDigitOnly(1) - 1
            val size = call.queryParams("size", "10").toDigitOnly(10)
            val localDate = LocalDate.now()
            val date = call.queryParams("date").toDigitOnly(localDate.dayOfMonth)
            val month = call.queryParams("month").toDigitOnly(localDate.monthValue)
            val year = call.queryParams("year").toDigitOnly(localDate.year)
            val queryDate = QueryDate(date, month, year)
            page = if (page <= 0) 0 else page
            userServices.getListAntrianById(id, queryDate, statusCode, page, size).also { call.respond(it) }
        }
    }
}