package id.my.mufidz.dao

import id.my.mufidz.model.AntreeQuery
import id.my.mufidz.model.Antrian
import id.my.mufidz.model.Paginate
import id.my.mufidz.model.dto.AntrianDTO
import id.my.mufidz.model.entity.AntrianEntity
import id.my.mufidz.model.table.AntrianTable
import id.my.mufidz.plugins.dbQuery
import id.my.mufidz.utils.localeDateNow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.and
import java.time.LocalDateTime

interface AntrianDao {
    suspend fun addAntrian(antrianDTO: AntrianDTO): Antrian
    suspend fun getAntrianById(antrianId: String): Antrian
    suspend fun updateStatusCode(antrianId: String, statusCode: Int, isVerify: Boolean): Antrian
    suspend fun checkAntrianById(antrianId: String): Boolean
    suspend fun listAntrianByQuery(antreeQuery: AntreeQuery): Paginate<Antrian>
}

class AntrianDaoImpl(private val statusDao: StatusDao) : AntrianDao {
    override suspend fun addAntrian(antrianDTO: AntrianDTO): Antrian = dbQuery {
        val antreeQuery = AntreeQuery(antrianDTO.merchantId, AntrianTable.merchantId)
        val list = baseListAntrianById(antreeQuery)
        val noUrut = list.size.plus(1)
        val remaining = list.filter { it.statusCode == 1 || it.statusCode == 2 }.size.plus(1)
        val newStatusCode = if (remaining == 1) 2 else antrianDTO.statusCode
        AntrianEntity.new(antrianDTO.id) {
            fromInsert(antrianDTO.copy(statusCode = newStatusCode))
            this.noUrut = noUrut
            this.remaining = remaining
        }.toAntrian().also {
            it.status = getStatusById(it.status.id)
        }
    }

    override suspend fun getAntrianById(antrianId: String): Antrian = dbQuery {
        AntrianEntity[antrianId].toAntrian().also {
            it.status = getStatusById(it.status.id)
        }
    }

    override suspend fun updateStatusCode(antrianId: String, statusCode: Int, isVerify: Boolean): Antrian = dbQuery {
        var antrianEntity: AntrianEntity? = null
        runBlocking {
            launch {
                delay(500)
                updateOtherRemaining(statusCode, antrianEntity)
            }
        }
        AntrianEntity[antrianId].also {
            antrianEntity = it
            val statusCodePermit = it.statusCode == 3 || it.statusCode == 4
            val isPermitVerify = it.remaining == 1 || statusCodePermit
            it.statusCode = when (statusCode) {
                5 -> if (isPermitVerify) 5 else it.statusCode
                else -> statusCode
            }
            it.updatedAt = Clock.System.localeDateNow()
            it.isVerify = if (isPermitVerify) isVerify else false
            it.remaining = if (statusCode == 2) it.remaining.minus(1) else 0
        }.toAntrian().also {
            it.status = getStatusById(it.status.id)
        }
    }

    private suspend fun updateOtherRemaining(statusCode: Int, antrianEntity: AntrianEntity?) {
        if (statusCode == 2) {
            antrianEntity?.let {
                val antreeQuery = AntreeQuery(it.merchantId.id.value, AntrianTable.merchantId)
                baseListAntrianById(antreeQuery).filter { antrianEntity -> antrianEntity.statusCode == 1 }
                    .map { antrianEntity ->
                        AntrianEntity[antrianEntity.id].apply {
                            updatedAt = Clock.System.localeDateNow()
                            remaining = antrianEntity.remaining.minus(1)
                        }
                    }
            }
        }
    }

    override suspend fun checkAntrianById(antrianId: String): Boolean = dbQuery {
        AntrianEntity.findById(antrianId) != null
    }

    private suspend fun baseListAntrianById(antreeQuery: AntreeQuery): List<AntrianEntity> = dbQuery {
        val dateQuery = antreeQuery.queryDate?.localDate ?: LocalDateTime.now().toKotlinLocalDateTime().date
        AntrianEntity.find {
            if (antreeQuery.statusCode == null) {
                antreeQuery.tableId eq antreeQuery.id
            } else {
                (antreeQuery.tableId eq antreeQuery.id) and (AntrianTable.statusCode eq antreeQuery.statusCode)
            }
        }.filter {
            (it.updatedAt ?: it.createdAt).toLocalDateTime().date == dateQuery
        }.toList()
    }

    override suspend fun listAntrianByQuery(
        antreeQuery: AntreeQuery
    ): Paginate<Antrian> = dbQuery {
        val skip = antreeQuery.size * antreeQuery.page
        var total: Int
        var list = emptyList<Antrian>()
        baseListAntrianById(antreeQuery).also { antrianEntityList ->
            total = antrianEntityList.size
            list = antrianEntityList.sortedWith(compareBy {
                (it.updatedAt ?: it.createdAt).toLocalDateTime().date
            }).drop(skip).take(antreeQuery.size).map {
                it.toAntrian().also { antrian ->
                    antrian.status = getStatusById(antrian.status.id)
                }
            }
        }
        Paginate(total, list)
    }

    private fun getStatusById(id: Int) = statusDao.getStatus(id)
}