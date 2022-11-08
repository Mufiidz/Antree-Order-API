package id.my.mufidz.response

import id.my.mufidz.base.BaseResponse
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ErrorWebResponse(
    override val code: Int,
    override val message: String,
    val path: String,
    val timestamp: Instant? = Clock.System.now()
) : BaseResponse
