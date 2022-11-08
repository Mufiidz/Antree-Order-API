package id.my.mufidz.model.table

import id.my.mufidz.base.StringIdTable
import id.my.mufidz.base.TableName

object MerchantTable : StringIdTable(TableName.MERCHANT) {
    val name = text("name")
    val username = text("username").uniqueIndex()
    val description = text("description")
    val isOpen = bool("is_open").default(false)
    val password = text("password")
    val salt = text("salt")
    override val createdAt = text(createdAtKey)
    override val updatedAt = text(updatedAtKey).nullable()
}