package id.my.mufidz.routes

import id.my.mufidz.response.WebResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.profileRoute() {
    route("/user") {
        get {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(WebResponse(HttpStatusCode.OK.value, "Success", "Your userId : $userId"))
        }
    }
}