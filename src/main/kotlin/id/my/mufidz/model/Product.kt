package id.my.mufidz.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String = "",
    val merchantId: String = "",
    val title: String = "",
    val category: String = "",
    val description: String = "",
    val quantity: Int = 1,
    val price: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)
