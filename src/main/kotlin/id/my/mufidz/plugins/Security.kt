package id.my.mufidz.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import id.my.mufidz.response.ErrorWebResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.configureSecurity() {

    authentication {
        jwt {
            val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
            val jwtIssuer = this@configureSecurity.environment.config.property("jwt.issuer").getString()
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(System.getenv("JWT_SECRET")))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString().isNotEmpty())
                    JWTPrincipal(credential.payload)
                else null
            }
            challenge { _, _ ->
                call.respond(
                    ErrorWebResponse(
                        HttpStatusCode.Unauthorized.value,
                        "Token is not valid or has expired",
                        call.request.path()
                    )
                )
            }
        }
    }

}
