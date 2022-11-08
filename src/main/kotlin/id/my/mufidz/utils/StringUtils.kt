package id.my.mufidz.utils

import io.ktor.server.application.*
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.format.DateTimeFormatter
import java.util.*

fun String.toDigitOnly(default: Int = 0): Int = this.filter { it.isDigit() }.toIntOrNull() ?: default

fun Clock.localeDateNow(): String = now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()

fun generateId(code: String = ""): String {
    val rawDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toJavaLocalDate()
    val date = rawDate.format(DateTimeFormatter.ofPattern("ddMMyy"))
    val uuid = UUID.randomUUID().toString().replace("-", "")
    val idWithCode = "$code-$date"
    val idTake = if (code.isEmpty()) 15 else 16
    return (if (code.isEmpty()) uuid else "$idWithCode$uuid").take(idTake)
}

fun ApplicationCall.queryParams(key: String, default: String? = null): String {
    val query = request.queryParameters[key]
    return if (default != null) query ?: default else query.orEmpty()
}

fun ApplicationCall.params(key: String, default: String? = null) : String {
    val params = parameters[key]
    return if (default != null) params ?: default else params.orEmpty()
}