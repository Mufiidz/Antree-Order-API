package id.my.mufidz.dao

import id.my.mufidz.model.Paginate
import id.my.mufidz.model.Product
import id.my.mufidz.model.dto.ProductDTO
import id.my.mufidz.model.entity.MerchantEntity
import id.my.mufidz.model.entity.ProductEntity
import id.my.mufidz.model.table.ProductTable
import id.my.mufidz.utils.localeDateNow
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction

interface ProductDao {
    suspend fun addProduct(product: Product): Product
    suspend fun checkProductById(productId: String) : Boolean
    suspend fun getProductById(productId: String): Product
    suspend fun getListProductByMerchantId(merchantId: String, page: Int, size: Int): Paginate<ProductDTO>
    suspend fun updateProduct(productId: String, product: ProductDTO): Product
    suspend fun deleteProduct(productId: String)
    suspend fun deleteAllProduct(): Int
}

class ProductDaoImpl : ProductDao {
    override suspend fun addProduct(product: Product): Product = transaction {
        ProductEntity.new(product.id) {
            merchantId = MerchantEntity[product.merchantId]
            title = product.title
            category = product.category
            description = product.description
            quantity = product.quantity
            price = product.price
            createdAt = Clock.System.localeDateNow()
        }.toProduct()
    }

    override suspend fun checkProductById(productId: String): Boolean = transaction {
        ProductEntity.findById(productId) != null
    }

    override suspend fun getProductById(productId: String): Product = transaction {
        ProductEntity[productId].toProduct()
    }

    override suspend fun getListProductByMerchantId(merchantId: String, page: Int, size: Int): Paginate<ProductDTO> =
        transaction {
            val newPage = if (page <= 0) 0 else page
            val newSize = if (size <= 5) 5 else size
            val skip = newSize * newPage
            var paginate: Paginate<ProductDTO>
            ProductEntity.find { ProductTable.merchantId eq merchantId }.also {
                val total = it.count().toInt()
                val list = it.limit(size, skip.toLong()).toList().map { productEntity -> productEntity.toProductDTO() }
                paginate = Paginate(total, list)
            }
            paginate
        }

    override suspend fun updateProduct(productId: String, product: ProductDTO): Product = transaction {
        ProductEntity[productId].also {
            it.title = product.title
            it.category = product.category
            it.description = product.description
            it.quantity = product.quantity
            it.price = product.price
            it.updatedAt = Clock.System.localeDateNow()
        }.toProduct()
    }

    override suspend fun deleteProduct(productId: String) = transaction {
        ProductEntity[productId].delete()
    }

    override suspend fun deleteAllProduct(): Int = transaction {
        ProductTable.deleteAll()
    }

}