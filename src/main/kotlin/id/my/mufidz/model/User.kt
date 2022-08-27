package id.my.mufidz.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val token: String = "",
    val createdAt: String? = null,
    val updatedAt: String? = null,
)
