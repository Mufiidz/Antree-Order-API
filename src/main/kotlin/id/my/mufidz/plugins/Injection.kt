package id.my.mufidz.plugins

import id.my.mufidz.di.servicesModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger

fun Application.configureInjection() {
    install(Koin) {
        SLF4JLogger()
        modules(servicesModule)
    }
}