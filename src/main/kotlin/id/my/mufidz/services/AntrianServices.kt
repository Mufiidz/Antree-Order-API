package id.my.mufidz.services

import id.my.mufidz.dao.*
import id.my.mufidz.model.Antrian
import id.my.mufidz.model.dto.AntrianDTO
import id.my.mufidz.response.WebResponse
import id.my.mufidz.routes.IdNotFoundException
import id.my.mufidz.utils.generateId
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*

interface AntrianServices {
    suspend fun addAntrian(antrianDTO: AntrianDTO): WebResponse<Antrian>
    suspend fun updateStatus(antrianId: String, statusId: Int, isVerify: Boolean = false): WebResponse<Antrian>
    suspend fun detailAntrian(antrianId: String): WebResponse<Antrian>
    suspend fun pickupAntrian(antrianId: String, isVerify: Boolean = false): WebResponse<Antrian>
}

class AntrianServicesImpl(
    private val merchantDao: MerchantDao,
    private val userDao: UserDao,
    private val orderDao: OrderDao,
    private val antrianDao: AntrianDao,
    private val statusDao: StatusDao
) : AntrianServices {

    override suspend fun addAntrian(antrianDTO: AntrianDTO): WebResponse<Antrian> {
        val antrianId: String = generateId("A")
        var antrian = Antrian()
        antrianDTO.copy(id = antrianId, statusCode = 1).apply {
            when {
                !merchantDao.checkMerchantById(merchantId) -> throw IdNotFoundException("MerchantId")
                !userDao.checkUserById(userId) -> throw IdNotFoundException("UserId")
                !merchantDao.checkStatusOpenMerchant(merchantId) -> throw BadRequestException("Merchant is close")
            }
            listOrder.mapIndexed { index, orderDTO ->
                when (val validation = orderDTO.validation()) {
                    is ValidationResult.Invalid -> throw BadRequestException("${validation.reasons.joinToString()} (Item ${index + 1})")
                    ValidationResult.Valid -> orderDTO
                }
            }
            antrianDao.addAntrian(this).also { antrian = it }
            listOrder.map {
                generateId("O").also { orderId ->
                    orderDao.addOrder(it.copy(id = orderId, antrianId = antrianId))
                }
            }
            antrian.orders = orderDao.listOrderByAntreId(antrianId)
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Berhasil membuat antrian", antrian
        )
    }

    override suspend fun updateStatus(antrianId: String, statusId: Int, isVerify: Boolean): WebResponse<Antrian> {
        when {
            antrianId.isEmpty() -> throw MissingRequestParameterException("Id")
            statusId <= 0 -> throw BadRequestException("statusId cant be less than 0")
            !antrianDao.checkAntrianById(antrianId) -> throw IdNotFoundException("AntrianId")
            !statusDao.checkStatusById(statusId) -> throw IdNotFoundException("StatusId")
            statusId == 5 -> throw BadRequestException("StatusId $statusId move to pickup endpoint")
        }
        val antrian = antrianDao.updateStatusCode(antrianId, statusId, isVerify)
        return WebResponse(
            HttpStatusCode.OK.value, "Success", antrian
        )
    }

    override suspend fun detailAntrian(antrianId: String): WebResponse<Antrian> {
        when {
            antrianId.isEmpty() -> throw MissingRequestParameterException("Id")
            !antrianDao.checkAntrianById(antrianId) -> throw IdNotFoundException("AntrianId")
        }
        val antrian = antrianDao.getAntrianById(antrianId)
        return WebResponse(
            HttpStatusCode.OK.value, "Success", antrian
        )
    }

    override suspend fun pickupAntrian(antrianId: String, isVerify: Boolean): WebResponse<Antrian> {
        when {
            antrianId.isEmpty() -> throw MissingRequestParameterException("Id")
            !antrianDao.checkAntrianById(antrianId) -> throw IdNotFoundException("AntrianId")
            !isVerify -> throw BadRequestException("Pickup order not verified")
        }
        var antrian = antrianDao.getAntrianById(antrianId)
        when (antrian.status.id) {
            1, 2 -> throw BadRequestException("Antrian not ready yet")
            3, 4 -> antrian = antrianDao.updateStatusCode(antrianId, 5, true)
            else -> throw BadRequestException("Cant be pickup verify")
        }
        return WebResponse(
            HttpStatusCode.OK.value, "Success", antrian
        )
    }
}