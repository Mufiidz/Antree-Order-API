package id.my.mufidz.model

data class RegisterMerchant(
    val id: String,
    val name: String,
    val desc: String = "",
    val username: String,
    val saltedHash: SaltedHash
)
