package id.my.mufidz.response

import id.my.mufidz.base.BaseResponse
import kotlinx.serialization.Serializable

@Serializable
data class WebResponse<T>(
    override val code: Int,
    override val message: String,
    val data: T,
) : BaseResponse
