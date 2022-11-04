package id.my.mufidz.model.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption

object ProductTable : IdTable<String>("product") {
    override val id: Column<EntityID<String>> = ProductTable.text("product_id").entityId().uniqueIndex()
    val merchantId = reference(
        "merchant_id", MerchantTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE, "fk_merchantId"
    )
    val title = ProductTable.text("name")
    val category = ProductTable.text("category").default("")
    val description = ProductTable.text("description").default("")
    val quantity = ProductTable.integer("quantity").default(1)
    val price = ProductTable.integer("price").default(0)
    val createdAt = ProductTable.text("created_at")
    val updatedAt = ProductTable.text("updated_at").nullable()
}