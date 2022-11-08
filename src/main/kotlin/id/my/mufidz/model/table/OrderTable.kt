package id.my.mufidz.model.table

import id.my.mufidz.base.StringIdTable
import id.my.mufidz.base.TableName
import org.jetbrains.exposed.sql.ReferenceOption

object OrderTable : StringIdTable(TableName.ORDER) {
    val antrianId = reference(
        AntrianTable.id.nameInDatabaseCase(),
        AntrianTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
        "fk_antrianId"
    )
    val productId = reference(
        ProductTable.id.nameInDatabaseCase(),
        ProductTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
        "fk_productId"
    )
    val note = text("note").default("")
    val quantity = integer("quantity").default(1)
    val price = integer("price").default(0)
    override val createdAt = text(createdAtKey)
    override val updatedAt = text(updatedAtKey).nullable()
}