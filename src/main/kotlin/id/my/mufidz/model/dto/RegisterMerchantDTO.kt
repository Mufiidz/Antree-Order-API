package id.my.mufidz.model.dto

import id.my.mufidz.base.BaseDataClass
import id.my.mufidz.utils.ValidationMessage
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class RegisterMerchantDTO(
    val name: String = "",
    val username: String = "",
    val description: String = "",
    val password: String = "",
) : BaseDataClass {

    override fun validation(): ValidationResult {
        val allMin = 3
        return when {
            name.isEmpty() -> ValidationMessage.isEmpty("Name")
            name.length < allMin -> ValidationMessage.min("name", allMin)
            username.isEmpty() -> ValidationMessage.isEmpty("username")
            username.length < allMin -> ValidationMessage.min("username", allMin)
            password.isEmpty() -> ValidationMessage.isEmpty("password")
            password.length < 8 -> ValidationMessage.min("password", 8)
            else -> ValidationResult.Valid
        }
    }
}
