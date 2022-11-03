package id.my.mufidz.model

data class Paginate<T>(
    val total: Int = 0,
    val list: List<T> = emptyList()
)
