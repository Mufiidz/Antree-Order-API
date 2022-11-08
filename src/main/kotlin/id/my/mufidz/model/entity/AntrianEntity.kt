package id.my.mufidz.model.entity

import id.my.mufidz.model.Antrian
import id.my.mufidz.model.Status
import id.my.mufidz.model.dto.AntrianDTO
import id.my.mufidz.model.table.AntrianTable
import id.my.mufidz.model.table.OrderTable
import id.my.mufidz.utils.localeDateNow
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AntrianEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, AntrianEntity>(AntrianTable)

    var merchantId by MerchantEntity referencedOn AntrianTable.merchantId
    var userId by UserEntity referencedOn AntrianTable.userId
    private var totalPrice by AntrianTable.totalPrice
    var statusCode by AntrianTable.statusCode
    var remaining by AntrianTable.remaining
    var noUrut by AntrianTable.noUrut
    var isVerify by AntrianTable.isVerify
    var createdAt by AntrianTable.createdAt
    var updatedAt by AntrianTable.updatedAt
    private val orders by OrderEntity referrersOn OrderTable.antrianId

    fun toAntrian() = Antrian(
        id.value,
        merchantId.id.value,
        userId.id.value,
        totalPrice,
        remaining,
        noUrut,
        isVerify,
        Status(statusCode),
        orders.toList().map { it.toOrderDto() },
        createdAt,
        updatedAt
    )

    fun fromInsert(antrianDTO: AntrianDTO) {
        merchantId = MerchantEntity[antrianDTO.merchantId]
        userId = UserEntity[antrianDTO.userId]
        statusCode = antrianDTO.statusCode
        totalPrice = antrianDTO.totalPrice ?: 0
        createdAt = Clock.System.localeDateNow()
    }
}