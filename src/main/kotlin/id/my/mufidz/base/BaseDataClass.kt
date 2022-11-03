package id.my.mufidz.base

import io.ktor.server.plugins.requestvalidation.*

interface BaseDataClass {
    fun validation(): ValidationResult
}