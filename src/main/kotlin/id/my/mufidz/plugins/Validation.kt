package id.my.mufidz.plugins

import id.my.mufidz.base.BaseDataClass
import id.my.mufidz.model.dto.LoginDTO
import id.my.mufidz.model.dto.ProductDTO
import id.my.mufidz.model.dto.RegisterDTO
import id.my.mufidz.model.dto.RegisterMerchantDTO
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        valid<RegisterMerchantDTO>()
        valid<RegisterDTO>()
        valid<LoginDTO>()
        valid<ProductDTO>()
    }
}

inline fun <reified T : BaseDataClass> RequestValidationConfig.valid() = validate<T> { it.validation() }

