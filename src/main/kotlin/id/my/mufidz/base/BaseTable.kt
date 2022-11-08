package id.my.mufidz.base

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object TableName {
    const val USER: String = "user"
    const val MERCHANT: String = "merchant"
    const val PRODUCT: String = "product"
    const val ORDER: String = "order"
    const val ANTRIAN: String = "antrian"
}

abstract class StringIdTable(name: String) : IdTable<String>(name) {
    protected val createdAtKey: String = "created_at"
    protected val updatedAtKey: String = "updated_at"
    final override val id: Column<EntityID<String>> = text("${name}_id").entityId().uniqueIndex()
    final override val primaryKey: PrimaryKey = PrimaryKey(id)
    abstract val createdAt: Column<String>
    abstract val updatedAt: Column<String?>
}
