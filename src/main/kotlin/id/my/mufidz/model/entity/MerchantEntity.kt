package id.my.mufidz.model.entity

import id.my.mufidz.model.Merchant
import id.my.mufidz.model.dto.MerchantDTO
import id.my.mufidz.model.table.MerchantTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MerchantEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, MerchantEntity>(MerchantTable)

    var name by MerchantTable.name
    var username by MerchantTable.username
    var description by MerchantTable.description
    var password by MerchantTable.password
    var salt by MerchantTable.salt
    var createdAt by MerchantTable.createdAt
    var updateAt by MerchantTable.updatedAt

    fun toMerchant(token: String = "") = Merchant(id.value, name, username, description, token, createdAt, updateAt)

    fun toMerchantDTO() = MerchantDTO(id.value, name, username, description)
}