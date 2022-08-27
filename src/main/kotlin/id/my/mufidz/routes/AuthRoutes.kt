package id.my.mufidz.routes

import id.my.mufidz.model.UserSession
import id.my.mufidz.model.dto.LoginDTO
import id.my.mufidz.model.dto.RegisterDTO
import id.my.mufidz.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.authRoute(application: Application) {

    val authServices by inject<AuthService>()

    route("/auth") {
        post<RegisterDTO>("/register") { registerDto ->
            authServices.register(registerDto).also { call.respond(it) }
        }
        post<LoginDTO>("/login") { loginDto ->
            authServices.login(loginDto, application).also {
                call.sessions.set(UserSession(it.data.id))
                call.respond(it)
            }
        }
        delete("/delete/{id}") {
            val id = call.parameters["id"].orEmpty()
            authServices.deleteUser(id).also { call.respond(it) }
        }
        authenticate {
            get("/logout") {
                authServices.logOut(call.sessions).also { call.respond(it) }
            }
        }
    }
}