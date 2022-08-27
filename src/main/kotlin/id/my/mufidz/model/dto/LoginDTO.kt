package id.my.mufidz.model.dto

import id.my.mufidz.base.BaseDataClass
import id.my.mufidz.utils.tryCatch
import kotlinx.serialization.Serializable
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

@Serializable
data class LoginDTO(
    val username: String = "",
    val password: String = "",
) : BaseDataClass {
    init {
        tryCatch { validation() }
    }

    override fun validation() {
        validate(this) {
            validate(LoginDTO::username).isNotBlank()
            validate(LoginDTO::password).isNotBlank()
        }
    }
}
