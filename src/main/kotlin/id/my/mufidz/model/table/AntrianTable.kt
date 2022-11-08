package id.my.mufidz.model.table

import id.my.mufidz.base.StringIdTable
import id.my.mufidz.base.TableName
import org.jetbrains.exposed.sql.ReferenceOption

object AntrianTable : StringIdTable(TableName.ANTRIAN) {
    val merchantId = reference(
        MerchantTable.id.nameInDatabaseCase(),
        MerchantTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
        "fk_merchantId"
    )
    val userId = reference(
        UserTable.id.nameInDatabaseCase(),
        UserTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
        "fk_userId"
    )
    val totalPrice = integer("total_price").default(0)
    val statusCode = integer("status_code").default(0)
    val remaining = integer("remaining").default(0)
    val noUrut = integer("nomor_antri").default(0)
    val isVerify = bool("is_verify_pickup").default(false)
    override val createdAt = text(createdAtKey)
    override val updatedAt = text(updatedAtKey).nullable()
}