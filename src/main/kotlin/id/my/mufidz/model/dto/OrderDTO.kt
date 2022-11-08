package id.my.mufidz.model.dto

import id.my.mufidz.base.BaseDataClass
import id.my.mufidz.utils.ValidationMessage
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class OrderDTO(
    val id: String = "",
    var antrianId: String = "",
    val productId: String = "",
    val note: String = "",
    val quantity: Int = 1,
    val price: Int? = null,
) : BaseDataClass {

    override fun validation(): ValidationResult = when {
        productId.isEmpty() -> ValidationMessage.isEmpty("productId")
        price == null -> ValidationMessage.isEmpty("price")
        else -> ValidationResult.Valid
    }
}
