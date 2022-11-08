package id.my.mufidz.model

import id.my.mufidz.model.dto.OrderDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Antrian(
    val id: String = "",
    val merchantId: String = "",
    val userId: String = "",
    val totalPrice: Int = 0,
    val remaining: Int = 0,
    @SerialName("nomor_antri")
    val noAntri: Int = 0,
    val isVerify: Boolean = false,
    var status: Status = Status(),
    var orders: List<OrderDTO> = emptyList(),
    val createdAt: String? = "",
    val updatedAt: String?= ""
)
