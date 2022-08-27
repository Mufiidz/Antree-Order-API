package id.my.mufidz.utils

import io.ktor.server.plugins.*
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage

fun tryCatch(run: () -> Unit) =
    try {
        run()
    } catch (e: ConstraintViolationException) {
        e.constraintViolations.mapToMessage().map {
            throw BadRequestException("${it.property}: ${it.message}")
        }
    }