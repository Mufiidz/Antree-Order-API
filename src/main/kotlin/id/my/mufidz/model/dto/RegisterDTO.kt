package id.my.mufidz.model.dto

import id.my.mufidz.base.BaseDataClass
import id.my.mufidz.utils.tryCatch
import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotBlank
import org.valiktor.validate

@Serializable
data class RegisterDTO(
    val name: String = "",
    val username: String = "",
    val password: String = "",
) : BaseDataClass {
    init {
        tryCatch { validation() }
    }

    override fun validation() {
        validate(this) {
            validate(RegisterDTO::name).isNotBlank().hasSize(min = 3)
            validate(RegisterDTO::username).isNotBlank().hasSize(min = 3)
            validate(RegisterDTO::password).isNotBlank().hasSize(min = 8)
        }
    }
}
