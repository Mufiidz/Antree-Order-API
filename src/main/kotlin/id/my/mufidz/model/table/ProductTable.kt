package id.my.mufidz.model.table

import id.my.mufidz.base.StringIdTable
import id.my.mufidz.base.TableName
import org.jetbrains.exposed.sql.ReferenceOption

object ProductTable : StringIdTable(TableName.PRODUCT) {
    val merchantId = reference(
        MerchantTable.id.nameInDatabaseCase(), MerchantTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE, "fk_merchantId"
    )
    val title = text("name")
    val category = text("category").default("")
    val description = text("description").default("")
    val quantity = integer("quantity").default(1)
    val price = integer("price").default(0)
    override val createdAt = text(createdAtKey)
    override val updatedAt = text(updatedAtKey).nullable()
}