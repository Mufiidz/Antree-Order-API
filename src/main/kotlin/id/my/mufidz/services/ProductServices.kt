package id.my.mufidz.services

import id.my.mufidz.dao.MerchantDao
import id.my.mufidz.dao.ProductDao
import id.my.mufidz.model.Product
import id.my.mufidz.model.dto.ProductDTO
import id.my.mufidz.plugins.dbQuery
import id.my.mufidz.response.WebResponse
import id.my.mufidz.routes.IdNotFoundException
import id.my.mufidz.utils.generateId
import id.my.mufidz.utils.localeDateNow
import io.ktor.http.*
import io.ktor.server.plugins.*
import kotlinx.datetime.Clock
import java.util.*

interface ProductServices {
    suspend fun addProduct(productDTO: ProductDTO): WebResponse<Product>
    suspend fun getDetailProductById(productId: String): WebResponse<Product?>
    suspend fun updateProduct(productId: String, productDTO: ProductDTO): WebResponse<Product?>
    suspend fun deleteProduct(productId: String): WebResponse<String>
}

class ProductServicesImpl(
    private val productDao: ProductDao,
    private val merchantDao: MerchantDao
) : ProductServices {

    override suspend fun addProduct(productDTO: ProductDTO): WebResponse<Product> {
        var product: Product
        val merchantId = productDTO.merchantId
        when {
            merchantId.isEmpty() -> throw BadRequestException("MerchantId is empty")
            !merchantDao.checkMerchantById(merchantId) -> throw IdNotFoundException("Merchant with id $merchantId not found")
            else -> {
                productDTO.toProduct().copy(
                    id = UUID.randomUUID().generateId(), createdAt = Clock.System.localeDateNow()
                ).also {
                    product = it
                    dbQuery {
                        productDao.addProduct(it)
                    }
                }
            }
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Berhasil menambahkan ${productDTO.title}", product
        )
    }

    override suspend fun getDetailProductById(productId: String): WebResponse<Product?> {
        var product: Product? = null
        when {
            productId.isEmpty() -> throw BadRequestException("ProductId is empty")
            !productDao.checkProductById(productId) -> throw IdNotFoundException("Product $productId tidak ditemukan")
            else -> dbQuery {
                product = productDao.getProductById(productId)
            }
        }
        val message = if (product != null) "Success" else "Product $productId tidak ditemukan"
        return WebResponse(
            HttpStatusCode.OK.value, message, product
        )
    }

    override suspend fun updateProduct(productId: String, productDTO: ProductDTO): WebResponse<Product?> {
        var product: Product? = null
        when {
            productId.isEmpty() -> throw BadRequestException("ProductId is empty")
            !productDao.checkProductById(productId) -> throw IdNotFoundException("Product $productId tidak ditemukan")
            else -> {
                dbQuery {
                    product = productDao.updateProduct(productId, productDTO)
                }
            }
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Berhasil memperbarui $productId", product
        )
    }

    override suspend fun deleteProduct(productId: String): WebResponse<String> {
        when {
            productId.isEmpty() -> throw BadRequestException("ProductId is empty")
            !productDao.checkProductById(productId) -> throw IdNotFoundException("Product $productId tidak ditemukan")
            else -> {
                dbQuery {
                    productDao.deleteProduct(productId)
                }
            }
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Success", "Berhasil menghapus $productId"
        )
    }
}