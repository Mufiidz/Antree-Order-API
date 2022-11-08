package id.my.mufidz.dao

import id.my.mufidz.model.Status

interface StatusDao {
    fun checkStatusById(statusId: Int) : Boolean
    fun getStatus(statusId: Int): Status
    fun getAllStatus(): List<Status>
}

class StatusDaoImpl : StatusDao {

    override fun checkStatusById(statusId: Int): Boolean = getAllStatus().find { it.id == statusId } != null

    override fun getStatus(statusId: Int): Status = getAllStatus().find { it.id == statusId } ?: Status()

    override fun getAllStatus(): List<Status> = listOf(
        "Dalam Antrian", "Dalam Proses", "Siap Diambil", "Dialihkan", "Selesai", "Dibatalkan"
    ).mapIndexed { index, message -> Status(index.plus(1), message) }
}