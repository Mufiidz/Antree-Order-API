package id.my.mufidz.model.dto

import id.my.mufidz.base.BaseDataClass
import id.my.mufidz.utils.ValidationMessage
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class AntrianDTO(
    val id: String = "",
    val merchantId: String = "",
    val userId: String = "",
    val statusCode: Int = 0,
    val listOrder: List<OrderDTO> = emptyList(),
    val totalPrice: Int? = null
) : BaseDataClass {

    override fun validation(): ValidationResult = when {
        merchantId.isEmpty() -> ValidationMessage.isEmpty("merchantId")
        userId.isEmpty() -> ValidationMessage.isEmpty("userId")
        listOrder.isEmpty() -> ValidationMessage.isEmpty("List Order")
        totalPrice == null -> ValidationMessage.isEmpty("totalPrice")
        else -> ValidationResult.Valid
    }
}
