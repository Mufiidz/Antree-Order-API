package id.my.mufidz.routes

import id.my.mufidz.model.dto.AntrianDTO
import id.my.mufidz.services.AntrianServices
import id.my.mufidz.utils.params
import id.my.mufidz.utils.queryParams
import id.my.mufidz.utils.toDigitOnly
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.antrianRoutes() {
    val antreeId = "id"
    val antrianServices by inject<AntrianServices>()

    route("antree") {
        post<AntrianDTO> { antrianDTO ->
            antrianServices.addAntrian(antrianDTO).also { call.respond(it) }
        }
        patch("/{id}") {
            val id = call.params(antreeId)
            val status = call.queryParams("statusId").toDigitOnly()
            antrianServices.updateStatus(id, status).also { call.respond(it) }
        }
        get("detail/{id}") {
            val id = call.params(antreeId)
            antrianServices.detailAntrian(id).also { call.respond(it) }
        }
        patch("/pickup/{id}") {
            val id = call.params(antreeId)
            val isVerify = call.queryParams("isVerify", "false").toBoolean()
            antrianServices.pickupAntrian(id, isVerify).also { call.respond(it) }
        }
    }
}