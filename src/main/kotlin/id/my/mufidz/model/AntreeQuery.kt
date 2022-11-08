package id.my.mufidz.model

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column

data class AntreeQuery(
    val id: String,
    val tableId: Column<EntityID<String>>,
    val statusCode: Int? = null,
    val queryDate: QueryDate? = null,
    val page: Int = 1,
    val size: Int = 10,
)
