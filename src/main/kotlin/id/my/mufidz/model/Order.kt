package id.my.mufidz.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: String = "",
    val antrianId: String = "",
    val productId: String = "",
    val note: String = "",
    val quantity: Int = 1,
    val totalPrice: Int = 0,
    val createdAt: String = "",
    val updatedAt: String = "",
)