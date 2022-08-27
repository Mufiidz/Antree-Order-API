package id.my.mufidz.response

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class WebResponse<T>(
    val code: Int,
    val message: String,
    val data: T,
)

@Serializable
data class ErrorWebResponse(
    val code: Int,
    val message: String,
    val path: String,
    val timestamp: Instant? = Clock.System.now()
)
