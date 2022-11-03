package id.my.mufidz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page(
    val size: Int,
    val total: Int,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("total_page") var totalPage: Int = total / size,
) {
    val safeTotalPage: Int
        get() = if (total % size == 0) totalPage else (totalPage + 1)
}
