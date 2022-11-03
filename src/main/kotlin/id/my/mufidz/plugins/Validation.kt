package id.my.mufidz.plugins

import id.my.mufidz.model.dto.LoginDTO
import id.my.mufidz.model.dto.RegisterDTO
import id.my.mufidz.model.dto.RegisterMerchantDTO
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<RegisterMerchantDTO> { it.validation() }
        validate<RegisterDTO> { it.validation() }
        validate<LoginDTO> { it.validation() }
    }
}

