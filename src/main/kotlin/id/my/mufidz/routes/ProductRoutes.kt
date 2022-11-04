package id.my.mufidz.routes

import id.my.mufidz.model.dto.ProductDTO
import id.my.mufidz.services.ProductServices
import id.my.mufidz.utils.params
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.productRoutes() {

    val productServices by inject<ProductServices>()

    route("products") {
        post<ProductDTO>{productDTO ->
            productServices.addProduct(productDTO).also { call.respond(it) }
        }
        get("/{id}") {
            val id = call.params("id")
            productServices.getDetailProductById(id).also { call.respond(it) }
        }
        put<ProductDTO>("/{id}") { productDto ->
            val id = call.params("id")
            productServices.updateProduct(id, productDto).also { call.respond(it) }
        }
        delete("/{id}") {
            val id = call.params("id")
            productServices.deleteProduct(id).also { call.respond(it) }
        }
    }
}