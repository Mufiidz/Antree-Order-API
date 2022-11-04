package id.my.mufidz.model.entity

import id.my.mufidz.model.Product
import id.my.mufidz.model.dto.ProductDTO
import id.my.mufidz.model.table.ProductTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProductEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, ProductEntity>(ProductTable)

    var merchantId by MerchantEntity referencedOn ProductTable.merchantId
    var title by ProductTable.title
    var category by ProductTable.category
    var description by ProductTable.description
    var quantity by ProductTable.quantity
    var price by ProductTable.price
    var createdAt by ProductTable.createdAt
    var updatedAt by ProductTable.updatedAt

    fun toProduct() = Product(id.value, merchantId.id.value, title, category, description, quantity, price, createdAt, updatedAt)

    fun toProductDTO() = ProductDTO(id.value, merchantId.id.value, title, category, description, quantity, price)
}