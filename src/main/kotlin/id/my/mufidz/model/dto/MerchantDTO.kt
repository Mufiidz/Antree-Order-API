package id.my.mufidz.model.dto

import id.my.mufidz.model.Product
import kotlinx.serialization.Serializable

@Serializable
data class MerchantDTO(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val description: String = "",
    val products: List<Product> = emptyList(),
)
