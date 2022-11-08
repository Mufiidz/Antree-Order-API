package id.my.mufidz.dao

import id.my.mufidz.model.Order
import id.my.mufidz.model.dto.OrderDTO
import id.my.mufidz.model.entity.OrderEntity
import id.my.mufidz.model.table.OrderTable
import id.my.mufidz.plugins.dbQuery

interface OrderDao {
    suspend fun addOrder(orderDTO: OrderDTO): Order
    suspend fun listOrderByAntreId(antrianId: String): List<OrderDTO>
    suspend fun deleteOrder(orderId: String)
}

class OrderDaoImpl : OrderDao {

    override suspend fun addOrder(orderDTO: OrderDTO): Order = dbQuery {
        OrderEntity.new(orderDTO.id) { fromInsert(orderDTO) }.toOrder()
    }

    override suspend fun listOrderByAntreId(antrianId: String): List<OrderDTO> = dbQuery {
        OrderEntity.find { OrderTable.antrianId eq antrianId }.map { it.toOrderDto() }
    }

    override suspend fun deleteOrder(orderId: String) = dbQuery {
        OrderEntity[orderId].delete()
    }

}