package id.my.mufidz.utils

import io.ktor.server.plugins.requestvalidation.*
import java.util.*

object ValidationMessage {
    fun isEmpty(name: String) =
        ValidationResult.Invalid("${name.titleCase()} must not be empty")
    fun min(name: String, min: Int) =
        ValidationResult.Invalid("${name.titleCase()} length must be greater than $min")

    private fun String.titleCase() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}