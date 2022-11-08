package id.my.mufidz.model.entity

import id.my.mufidz.model.Order
import id.my.mufidz.model.dto.OrderDTO
import id.my.mufidz.model.table.OrderTable
import id.my.mufidz.utils.localeDateNow
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, OrderEntity>(OrderTable)

    private var antrianId by AntrianEntity referencedOn OrderTable.antrianId
    private var productId by ProductEntity referencedOn OrderTable.productId
    private var note by OrderTable.note
    private var quantity by OrderTable.quantity
    private var price by OrderTable.price
    var createdAt by OrderTable.createdAt
    var updatedAt by OrderTable.updatedAt

    fun toOrder() = Order(id.value, antrianId.id.value, productId.id.value, note, quantity, price, createdAt)

    fun fromInsert(order: OrderDTO) {
        antrianId = AntrianEntity[order.antrianId]
        productId = ProductEntity[order.productId]
        note = order.note
        quantity = order.quantity
        price = order.price ?: 0
        createdAt = Clock.System.localeDateNow()
    }

    fun toOrderDto() = OrderDTO(id.value, antrianId.id.value, productId.id.value, note, quantity, price)
}