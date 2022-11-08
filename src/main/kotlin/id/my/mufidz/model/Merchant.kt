package id.my.mufidz.model

import kotlinx.serialization.Serializable

@Serializable
data class Merchant(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val description: String = "",
    val isOpen: Boolean = false,
    val token: String = "",
    val products: List<Product> = emptyList(),
    val antrians: List<Antrian> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null,
)
