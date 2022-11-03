package id.my.mufidz.utils

fun String.toDigitOnly(default: Int = 0): Int = this.filter { it.isDigit() }.toIntOrNull() ?: default