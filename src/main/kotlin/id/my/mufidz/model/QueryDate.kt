package id.my.mufidz.model

import kotlinx.datetime.LocalDate

data class QueryDate(
    val date: Int = 1,
    val month: Int = 1,
    val year: Int = 2022,
) {
    val localDate: LocalDate = LocalDate(year, month, date)
}
