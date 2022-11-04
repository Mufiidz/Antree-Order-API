package id.my.mufidz.model.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object MerchantTable : IdTable<String>("merchant") {
    override val id: Column<EntityID<String>> = MerchantTable.text("merchant_id").entityId().uniqueIndex()
    val name = MerchantTable.text("name")
    val username = MerchantTable.text("username").uniqueIndex()
    val description = MerchantTable.text("description")
    val password = MerchantTable.text("password")
    val salt = MerchantTable.text("salt")
    val createdAt = MerchantTable.text("created_at")
    val updatedAt = MerchantTable.text("updated_at").nullable()
}