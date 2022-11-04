package id.my.mufidz.utils

import io.ktor.server.application.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.*

fun String.toDigitOnly(default: Int = 0): Int = this.filter { it.isDigit() }.toIntOrNull() ?: default

fun Clock.localeDateNow(): String = now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()

fun UUID.generateId(): String {
    val id = toString().replace("-", "")
    return if (id.length > 15) id.take(15) else id
}

fun ApplicationCall.queryParams(key: String, default: String? = null): String {
    val query = request.queryParameters[key]
    return if (default != null) query ?: default else query.orEmpty()
}

fun ApplicationCall.params(key: String, default: String? = null) : String {
    val params = parameters[key]
    return if (default != null) params ?: default else params.orEmpty()
}