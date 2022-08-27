package id.my.mufidz.routes

import id.my.mufidz.response.ErrorWebResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException
import org.valiktor.ConstraintViolationException

fun StatusPagesConfig.errorRoute() {
    exception<AuthenticationException> { call, cause ->
        call.respond(
            HttpStatusCode.Unauthorized,
            ErrorWebResponse(
                HttpStatusCode.Unauthorized.value,
                cause.message ?: HttpStatusCode.Unauthorized.description,
                call.request.path()
            )
        )
    }
    status(HttpStatusCode.Unauthorized) { call, status ->
        call.respond(
            HttpStatusCode.Unauthorized,
            ErrorWebResponse(status.value, status.description, call.request.path())
        )
    }
    exception<AuthorizationException> { call, cause ->
        call.respond(
            HttpStatusCode.Forbidden,
            ErrorWebResponse(
                HttpStatusCode.Forbidden.value,
                cause.message ?: HttpStatusCode.Forbidden.description,
                call.request.path()
            )
        )
    }
    exception<BadRequestException> { call, cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorWebResponse(
                HttpStatusCode.BadRequest.value,
                cause.message ?: HttpStatusCode.BadRequest.description,
                call.request.path()
            )
        )
    }
    status(HttpStatusCode.MethodNotAllowed) { call, status ->
        call.respond(
            HttpStatusCode.MethodNotAllowed,
            ErrorWebResponse(status.value, status.description, call.request.path())
        )
    }
    status(HttpStatusCode.NotFound) { call, status ->
        call.respond(HttpStatusCode.NotFound, ErrorWebResponse(status.value, status.description, call.request.path()))
    }
    status(HttpStatusCode.UnsupportedMediaType) { call, status ->
        call.respond(
            HttpStatusCode.UnsupportedMediaType,
            ErrorWebResponse(status.value, status.description, call.request.path())
        )
    }
    status(HttpStatusCode.InternalServerError) { call, status ->
        call.respond(
            HttpStatusCode.InternalServerError,
            ErrorWebResponse(status.value, status.description, call.request.path())
        )
    }
    exception<SerializationException> { call, cause ->
        call.respond(
            HttpStatusCode.UnprocessableEntity,
            ErrorWebResponse(
                HttpStatusCode.UnprocessableEntity.value,
                cause.message ?: HttpStatusCode.UnprocessableEntity.description,
                call.request.path()
            )
        )
    }
    exception { call: ApplicationCall, cause: ConstraintViolationException ->
        call.respond(
            HttpStatusCode.BadRequest,
            ErrorWebResponse(
                HttpStatusCode.BadRequest.value,
                cause.message ?: HttpStatusCode.BadRequest.description,
                call.request.path()
            )
        )
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()