package id.my.mufidz.model.dto

import id.my.mufidz.base.BaseDataClass
import id.my.mufidz.model.Product
import id.my.mufidz.utils.ValidationMessage
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    val id: String = "",
    val merchantId: String = "",
    val title: String = "",
    val category: String = "",
    val description: String = "",
    val quantity: Int = 1,
    val price: Int = 0,
) : BaseDataClass {

    override fun validation(): ValidationResult = when {
        title.isEmpty() -> ValidationMessage.isEmpty("title")
        merchantId.isEmpty() -> ValidationMessage.isEmpty("merchantId")
        quantity < 1 -> ValidationMessage.min("quantity", 1)
        price < 0 -> ValidationMessage.min("quantity", 0)
        else -> ValidationResult.Valid
    }

    fun toProduct() : Product = Product(id, merchantId, title, category, description, quantity, price)
}
