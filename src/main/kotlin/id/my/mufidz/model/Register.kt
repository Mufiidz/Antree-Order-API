package id.my.mufidz.model

data class Register(val id: String, val name: String, val username: String, val saltedHash: SaltedHash)
