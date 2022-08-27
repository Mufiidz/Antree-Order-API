package id.my.mufidz.model

data class TokenConfig(
    val issuer: String,
    val audience: String,
    val secret: String
)
