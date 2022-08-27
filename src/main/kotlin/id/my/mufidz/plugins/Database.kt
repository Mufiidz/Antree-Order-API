package id.my.mufidz.plugins

import id.my.mufidz.model.table.UserTable
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exposedLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    environment.config.apply {
        val driverClassName = property("ktor.storage.driverClassName").getString()
        val jdbcURL = property("ktor.storage.jdbcURL").getString()
        val user = property("ktor.storage.user").getString()
        val password = property("ktor.storage.password").getString()
        val defaultDatabase = property("ktor.storage.database").getString()
        exposedLogger.info("Init DB $jdbcURL")
        Database.connect(
            url = "$jdbcURL/$defaultDatabase",
            driver = driverClassName,
            user = user,
            password = password
        ).also {
            transaction(it) {
                SchemaUtils.create(UserTable)
            }
        }
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }