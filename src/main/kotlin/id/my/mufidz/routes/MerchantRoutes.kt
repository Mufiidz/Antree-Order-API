package id.my.mufidz.routes

import id.my.mufidz.services.MerchantService
import id.my.mufidz.utils.params
import id.my.mufidz.utils.queryParams
import id.my.mufidz.utils.toDigitOnly
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.merchantRoutes() {

    val merchantService by inject<MerchantService>()

    route("/merchants") {
        get {
            var page = call.queryParams("page", "1").toDigitOnly(1) - 1
            val size = call.queryParams("size", "10").toDigitOnly(10)
            page = if (page <= 0) 0 else page
            merchantService.getAllMerchant(page, size).also { call.respond(it) }
        }
        get("/detail/{id}") {
            val id = call.params("id")
            merchantService.getDetailMerchant(id).also { call.respond(it) }
        }
        get("/products") {
            val id = call.queryParams("id")
            var page = call.queryParams("page", "1").toDigitOnly(1) - 1
            val size = call.queryParams("size", "10").toDigitOnly(10)
            page = if (page <= 0) 0 else page
            merchantService.getAllProductByMerchantId(id, page, size).also { call.respond(it) }
        }
    }
}