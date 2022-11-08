package id.my.mufidz.services

import id.my.mufidz.dao.MerchantDao
import id.my.mufidz.dao.ProductDao
import id.my.mufidz.model.Product
import id.my.mufidz.model.dto.ProductDTO
import id.my.mufidz.plugins.dbQuery
import id.my.mufidz.response.WebResponse
import id.my.mufidz.routes.IdNotFoundException
import id.my.mufidz.utils.localeDateNow
import id.my.mufidz.utils.generateId
import io.ktor.http.*
import io.ktor.server.plugins.*
import kotlinx.datetime.Clock

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
            merchantId.isEmpty() -> throw MissingRequestParameterException("MerchantId")
            !merchantDao.checkMerchantById(merchantId) -> throw IdNotFoundException("MerchantId")
            else -> {
                productDTO.toProduct().copy(
                    id = generateId("P"), createdAt = Clock.System.localeDateNow()
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
        val product: Product?
        when {
            productId.isEmpty() -> throw MissingRequestParameterException("ProductId")
            !productDao.checkProductById(productId) -> throw IdNotFoundException("ProductId")
            else -> product = productDao.getProductById(productId)
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Success", product
        )
    }

    override suspend fun updateProduct(productId: String, productDTO: ProductDTO): WebResponse<Product?> {
        val product: Product?
        when {
            productId.isEmpty() -> throw MissingRequestParameterException("ProductId")
            !productDao.checkProductById(productId) -> throw IdNotFoundException("ProductId")
            else -> product = productDao.updateProduct(productId, productDTO)
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Berhasil memperbarui $productId", product
        )
    }

    override suspend fun deleteProduct(productId: String): WebResponse<String> {
        when {
            productId.isEmpty() -> throw MissingRequestParameterException("ProductId")
            !productDao.checkProductById(productId) -> throw IdNotFoundException("ProductId")
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