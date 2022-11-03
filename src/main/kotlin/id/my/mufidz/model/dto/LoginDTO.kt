package id.my.mufidz.model.dto

import id.my.mufidz.base.BaseDataClass
import id.my.mufidz.utils.ValidationMessage
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(
    val username: String = "",
    val password: String = "",
) : BaseDataClass {

    override fun validation(): ValidationResult = when {
        username.isEmpty() -> ValidationMessage.isEmpty("username")
        password.isEmpty() -> ValidationMessage.isEmpty("password")
        else -> ValidationResult.Valid
    }
}
