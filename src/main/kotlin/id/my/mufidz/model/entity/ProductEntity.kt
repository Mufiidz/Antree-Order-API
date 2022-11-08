package id.my.mufidz.model.entity

import id.my.mufidz.model.Product
import id.my.mufidz.model.dto.ProductDTO
import id.my.mufidz.model.table.ProductTable
import id.my.mufidz.utils.localeDateNow
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProductEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, ProductEntity>(ProductTable)

    var merchantId by MerchantEntity referencedOn ProductTable.merchantId
    private var title by ProductTable.title
    private var category by ProductTable.category
    var description by ProductTable.description
    private var quantity by ProductTable.quantity
    private var price by ProductTable.price
    var createdAt by ProductTable.createdAt
    var updatedAt by ProductTable.updatedAt

    fun toProduct() = Product(id.value, merchantId.id.value, title, category, description, quantity, price, createdAt, updatedAt)

    fun toProductDTO() = ProductDTO(id.value, merchantId.id.value, title, category, description, quantity, price)
    fun fromInsert(product: Product) {
        merchantId = MerchantEntity[product.merchantId]
        title = product.title
        category = product.category
        description = product.description
        quantity = product.quantity
        price = product.price
        createdAt = Clock.System.localeDateNow()
    }

    fun fromUpdate(productDTO: ProductDTO) {
        title = productDTO.title
        category = productDTO.category
        description = productDTO.description
        quantity = productDTO.quantity
        price = productDTO.price
        updatedAt = Clock.System.localeDateNow()
    }
}