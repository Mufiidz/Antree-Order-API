package id.my.mufidz.model.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object UserTable : IdTable<String>("user") {
    override val id: Column<EntityID<String>> = text("user_id").entityId().uniqueIndex()
    val name = text("name")
    val username = text("username").uniqueIndex()
    val password = text("password")
    val salt = text("salt")
    val createdAt = text("created_at")
    val updatedAt = text("updated_at").nullable()
}