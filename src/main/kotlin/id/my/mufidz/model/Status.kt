package id.my.mufidz.model

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val id: Int = 0,
    val message: String = "NO STATUS",
)
