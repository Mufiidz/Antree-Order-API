package id.my.mufidz.response

import id.my.mufidz.base.BaseResponse
import id.my.mufidz.model.Page
import kotlinx.serialization.Serializable

@Serializable
data class PaginateWebResponse<T>(
    override val code: Int,
    override val message: String,
    val data: T,
    val page: Page
) : BaseResponse
