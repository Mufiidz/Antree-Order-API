package id.my.mufidz.dao

import id.my.mufidz.model.Paginate
import id.my.mufidz.model.Product
import id.my.mufidz.model.dto.ProductDTO
import id.my.mufidz.model.entity.ProductEntity
import id.my.mufidz.model.table.ProductTable
import id.my.mufidz.plugins.dbQuery
import org.jetbrains.exposed.sql.deleteAll

interface ProductDao {
    suspend fun addProduct(product: Product): Product
    suspend fun checkProductById(productId: String): Boolean
    suspend fun getProductById(productId: String): Product
    suspend fun getListProductByMerchantId(merchantId: String, page: Int, size: Int): Paginate<ProductDTO>
    suspend fun updateProduct(productId: String, product: ProductDTO): Product
    suspend fun deleteProduct(productId: String)
    suspend fun deleteAllProduct(): Int
}

class ProductDaoImpl : ProductDao {
    override suspend fun addProduct(product: Product): Product = dbQuery {
        ProductEntity.new(product.id) { fromInsert(product) }.toProduct()
    }

    override suspend fun checkProductById(productId: String): Boolean = dbQuery {
        ProductEntity.findById(productId) != null
    }

    override suspend fun getProductById(productId: String): Product = dbQuery {
        ProductEntity[productId].toProduct()
    }

    override suspend fun getListProductByMerchantId(merchantId: String, page: Int, size: Int): Paginate<ProductDTO> =
        dbQuery {
            val skip = size * page
            var paginate: Paginate<ProductDTO>
            ProductEntity.find { ProductTable.merchantId eq merchantId }.also {
                val total = it.count().toInt()
                val list = it.limit(size, skip.toLong()).toList().map { productEntity -> productEntity.toProductDTO() }
                paginate = Paginate(total, list)
            }
            paginate
        }

    override suspend fun updateProduct(productId: String, product: ProductDTO): Product = dbQuery {
        ProductEntity[productId].also { it.fromUpdate(product) }.toProduct()
    }

    override suspend fun deleteProduct(productId: String) = dbQuery {
        ProductEntity[productId].delete()
    }

    override suspend fun deleteAllProduct(): Int = dbQuery {
        ProductTable.deleteAll()
    }

}