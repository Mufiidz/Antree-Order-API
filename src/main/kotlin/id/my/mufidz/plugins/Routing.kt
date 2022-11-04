package id.my.mufidz.plugins

import id.my.mufidz.routes.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import kotlinx.html.body
import kotlinx.html.h1

fun Application.configureRouting() {
    install(StatusPages) { errorRoute() }
    routing {
//        route("/") {
//            get {
//                call.respondRedirect("/antree-order")
//            }
//        }
        route("/antree-order") {
            get {
                if (call.principal<JWTPrincipal>() == null) {
                    call.respondHtml {
                        body {
                            h1 {
                                +"Antree Order API v1"
                            }
                            +"Basic API using KTOR"
                        }
                    }
                } else {
                    call.respondRedirect("/api/user")
                }
            }
            authRoutes(this@configureRouting)
            authenticate {
                profileRoute()
                merchantRoutes()
                productRoutes()
            }
        }
    }
}
