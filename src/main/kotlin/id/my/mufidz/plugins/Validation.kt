package id.my.mufidz.plugins

import id.my.mufidz.base.BaseDataClass
import id.my.mufidz.model.dto.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        valid<RegisterMerchantDTO>()
        valid<RegisterDTO>()
        valid<LoginDTO>()
        valid<ProductDTO>()
        valid<AntrianDTO>()
        valid<OrderDTO>()
    }
}

inline fun <reified T : BaseDataClass> RequestValidationConfig.valid() = validate<T> { it.validation() }

