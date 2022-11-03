package id.my.mufidz.routes

import id.my.mufidz.model.UserSession
import id.my.mufidz.model.dto.LoginDTO
import id.my.mufidz.model.dto.RegisterDTO
import id.my.mufidz.model.dto.RegisterMerchantDTO
import id.my.mufidz.services.MerchantAuthService
import id.my.mufidz.services.UserAuthService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.authRoute(application: Application) {

    val userAuthServices by inject<UserAuthService>()
    val merchantAuthService by inject<MerchantAuthService>()

    route("/auth") {
        userRoute(application, userAuthServices)
        merchantRoute(application, merchantAuthService)
    }
}

fun Route.merchantRoute(application: Application, merchantAuthService: MerchantAuthService) = route("/merchant") {
    post<RegisterMerchantDTO>("/register") { registerMerchantDTO ->
        merchantAuthService.register(registerMerchantDTO).also { call.respond(it) }
    }
    post<LoginDTO>("/login") { loginDto ->
        merchantAuthService.login(loginDto, application).also { call.respond(it) }
    }
    delete("/delete/{id}") {
        val id = call.parameters["id"].orEmpty()
        merchantAuthService.deleteUser(id).also { call.respond(it) }
    }
    authenticate {
        get("/logout") {
            merchantAuthService.logOut(call.sessions).also { call.respond(it) }
        }
    }
}


fun Route.userRoute(application: Application, userAuthServices: UserAuthService) = route("/user") {
    post<RegisterDTO>("/register") { registerDto ->
        userAuthServices.register(registerDto).also { call.respond(it) }
    }
    post<LoginDTO>("/login") { loginDto ->
        userAuthServices.login(loginDto, application).also {
            call.sessions.set(UserSession(it.data.id))
            call.respond(it)
        }
    }
    delete("/delete/{id}") {
        val id = call.parameters["id"].orEmpty()
        userAuthServices.deleteUser(id).also { call.respond(it) }
    }
    authenticate {
        get("/logout") {
            userAuthServices.logOut(call.sessions).also { call.respond(it) }
        }
    }
}