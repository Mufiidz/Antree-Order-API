package id.my.mufidz.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class MerchantDTO(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val description: String = "",
    val isOpen: Boolean = false
)
