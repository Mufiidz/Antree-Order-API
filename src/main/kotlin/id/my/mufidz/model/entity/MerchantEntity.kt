package id.my.mufidz.model.entity

import id.my.mufidz.model.Merchant
import id.my.mufidz.model.RegisterMerchant
import id.my.mufidz.model.dto.MerchantDTO
import id.my.mufidz.model.table.AntrianTable
import id.my.mufidz.model.table.MerchantTable
import id.my.mufidz.model.table.ProductTable
import id.my.mufidz.utils.localeDateNow
import io.ktor.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MerchantEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, MerchantEntity>(MerchantTable)

    var name by MerchantTable.name
    var username by MerchantTable.username
    var description by MerchantTable.description
    var isOpen by MerchantTable.isOpen
    var password by MerchantTable.password
    var salt by MerchantTable.salt
    var createdAt by MerchantTable.createdAt
    private var updateAt by MerchantTable.updatedAt
    private val products by ProductEntity referrersOn ProductTable.merchantId
    private val antrians by AntrianEntity referrersOn AntrianTable.merchantId

    fun toMerchantWithToken(token: String) = Merchant(
        id.value, name, username, description, isOpen, token, createdAt = createdAt, updatedAt = updateAt
    )

    fun toMerchant() = Merchant(
        id.value,
        name,
        username,
        description,
        isOpen,
        products = products.limit(5).toList().map { it.toProduct() },
        antrians = antrians.limit(5).toList().map { it.toAntrian() },
        createdAt = createdAt,
        updatedAt = updateAt
    )

    fun toMerchantDTO() = MerchantDTO(
        id.value, name, username, description, isOpen
    )

    fun fromRegisterMerchant(registerMerchant: RegisterMerchant) {
        name = registerMerchant.name
        username = registerMerchant.username.toLowerCasePreservingASCIIRules()
        description = registerMerchant.desc
        password = registerMerchant.saltedHash.hash
        salt = registerMerchant.saltedHash.salt
        createdAt = Clock.System.localeDateNow()
    }
}