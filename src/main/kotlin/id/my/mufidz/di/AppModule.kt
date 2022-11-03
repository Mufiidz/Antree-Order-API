package id.my.mufidz.di

import id.my.mufidz.dao.MerchantDao
import id.my.mufidz.dao.MerchantDaoImpl
import id.my.mufidz.dao.UserDao
import id.my.mufidz.dao.UserDaoImpl
import id.my.mufidz.security.hashing.HashingService
import id.my.mufidz.security.hashing.HashingServiceImpl
import id.my.mufidz.security.token.TokenService
import id.my.mufidz.security.token.TokenServiceImpl
import id.my.mufidz.services.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val servicesModule = module {
    singleOf(::MerchantAuthServiceImpl) { bind<MerchantAuthService>() }
    singleOf(::UserAuthServiceImpl) { bind<UserAuthService>() }
    singleOf(::HashingServiceImpl) { bind<HashingService>() }
    singleOf(::TokenServiceImpl) { bind<TokenService>() }
    singleOf(::MerchantServicesImpl) { bind<MerchantService>() }
}

val daoModule = module {
    singleOf(::UserDaoImpl) { bind<UserDao>() }
    singleOf(::MerchantDaoImpl) { bind<MerchantDao>() }
}