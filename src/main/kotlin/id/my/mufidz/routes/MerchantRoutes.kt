package id.my.mufidz.routes

import id.my.mufidz.services.MerchantService
import id.my.mufidz.utils.toDigitOnly
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.merchantRoutes() {

    val merchantService by inject<MerchantService>()

    route("/merchants") {
        get {
            val pageParam = call.request.queryParameters["page"] ?: "1"
            val sizeParam = call.request.queryParameters["size"] ?: "10"
            var page = pageParam.toDigitOnly(1) - 1
            val size = sizeParam.toDigitOnly(10)
            page = if (page <= 0) 0 else page
            merchantService.getAllMerchant(page, size).also { call.respond(it) }
        }
    }
}