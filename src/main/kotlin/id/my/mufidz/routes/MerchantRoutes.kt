package id.my.mufidz.routes

import id.my.mufidz.model.QueryDate
import id.my.mufidz.services.MerchantService
import id.my.mufidz.utils.params
import id.my.mufidz.utils.queryParams
import id.my.mufidz.utils.toDigitOnly
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDate

fun Route.merchantRoutes() {

    val merchantService by inject<MerchantService>()
    val merchantId = "merchantId"
    val pageKey = "page"
    val sizeKey = "size"

    route("/merchants") {
        get {
            var page = call.queryParams(pageKey, "1").toDigitOnly(1) - 1
            val size = call.queryParams(sizeKey, "10").toDigitOnly(10)
            page = if (page <= 0) 0 else page
            merchantService.getAllMerchant(page, size).also { call.respond(it) }
        }
        get("/detail/{$merchantId}") {
            val id = call.params(merchantId)
            merchantService.getDetailMerchant(id).also { call.respond(it) }
        }
        patch("/detail/{$merchantId}") {
            val id = call.params(merchantId)
            val isOpen = call.queryParams("isOpen", "false").toBoolean()
            merchantService.updateisOpenMerchant(id, isOpen).also { call.respond(it) }
        }
        get("/products/{$merchantId}") {
            val id = call.params(merchantId)
            var page = call.queryParams(pageKey, "1").toDigitOnly(1) - 1
            val size = call.queryParams(sizeKey, "10").toDigitOnly(10)
            page = if (page <= 0) 0 else page
            merchantService.getAllProductByMerchantId(id, page, size).also { call.respond(it) }
        }
        get("antree/{$merchantId}") {
            val id = call.params(merchantId)
            val statusCode = call.queryParams("statusId").filter { it.isDigit() }.toIntOrNull()
            var page = call.queryParams(pageKey, "1").toDigitOnly(1) - 1
            val size = call.queryParams(sizeKey, "10").toDigitOnly(10)
            val localDate = LocalDate.now()
            val date = call.queryParams("date").toDigitOnly(localDate.dayOfMonth)
            val month = call.queryParams("month").toDigitOnly(localDate.monthValue)
            val year = call.queryParams("year").toDigitOnly(localDate.year)
            val queryDate = QueryDate(date, month, year)
            page = if (page <= 0) 0 else page
            merchantService.getAllAntrianByIdMerchant(id, queryDate, statusCode, page, size).also { call.respond(it) }
        }
    }
}