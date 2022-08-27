package id.my.mufidz.di

import id.my.mufidz.dao.UserDao
import id.my.mufidz.dao.UserDaoImpl
import id.my.mufidz.security.hashing.HashingService
import id.my.mufidz.security.hashing.HashingServiceImpl
import id.my.mufidz.security.token.TokenService
import id.my.mufidz.security.token.TokenServiceImpl
import id.my.mufidz.services.AuthService
import id.my.mufidz.services.AuthServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val servicesModule = module {
    singleOf(::UserDaoImpl) { bind<UserDao>() }
    singleOf(::AuthServiceImpl) { bind<AuthService>() }
    singleOf(::HashingServiceImpl) { bind<HashingService>() }
    singleOf(::TokenServiceImpl) { bind<TokenService>() }
}