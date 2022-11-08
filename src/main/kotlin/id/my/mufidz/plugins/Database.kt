package id.my.mufidz.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import id.my.mufidz.model.table.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    DatabaseFactory.init(environment.config).also {
        transaction(it) {
            SchemaUtils.createMissingTablesAndColumns(
                UserTable, MerchantTable, ProductTable, OrderTable, AntrianTable
            )
        }
    }
}

object DatabaseFactory {
    fun init(appConfig: ApplicationConfig): Database = Database.connect(hikari(appConfig))

    private fun hikari(appConfig: ApplicationConfig): HikariDataSource {
        val hikariConfig: HikariConfig
        appConfig.apply {
            val driverClassName = property("ktor.storage.driverClassName").getString()
            val jdbcURL = property("ktor.storage.jdbcURL").getString()
            val user = property("ktor.storage.user").getString()
            val pass = property("ktor.storage.password").getString()
            val defaultDatabase = property("ktor.storage.database").getString()
            hikariConfig = HikariConfig().apply {
                jdbcUrl = "$jdbcURL/$defaultDatabase"
                setDriverClassName(driverClassName)
                username = user
                password = pass
                isAutoCommit = false
                maximumPoolSize = 3
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            }
        }
        return HikariDataSource(hikariConfig)
    }
}

suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }