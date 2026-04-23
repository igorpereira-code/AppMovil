package ucb.edu.bo.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ucb.edu.bo.dollar.data.datasource.DollarLocalDataSource
import ucb.edu.bo.dollar.data.repository.DollarRepositoryImpl
import ucb.edu.bo.dollar.data.service.DbService
import ucb.edu.bo.dollar.domain.repository.DollarRepository
import ucb.edu.bo.events.data.datasource.EventLocalDataSource
import ucb.edu.bo.events.data.repository.EventRepositoryImpl
import ucb.edu.bo.events.data.service.EventDbService
import ucb.edu.bo.events.domain.repository.EventRepository
import ucb.edu.bo.firebase.RemoteConfigManager
import ucb.edu.bo.realtimedatabasecmp.data.datasource.FirebaseDataSource
import ucb.edu.bo.realtimedatabasecmp.data.repository.FirebaseTestRepositoryImpl
import ucb.edu.bo.realtimedatabasecmp.domain.repository.FirebaseTestRepository
import ucb.edu.bo.remoteconfig.data.repository.RemoteConfigRepositoryImpl
import ucb.edu.bo.remoteconfig.domain.repository.RemoteConfigRepository

val dataModule = module {
    singleOf(::DollarRepositoryImpl).bind<DollarRepository>()
    singleOf(::DbService).bind<DollarLocalDataSource>()
    singleOf(::DollarRepositoryImpl).bind<DollarRepository>()
    singleOf(::DbService).bind<DollarLocalDataSource>()
    factory { FirebaseDataSource() }
    factory<FirebaseTestRepository> { FirebaseTestRepositoryImpl(get()) }
    single { RemoteConfigManager() }
    single<RemoteConfigRepository> { RemoteConfigRepositoryImpl(get()) }
    singleOf(::EventRepositoryImpl).bind<EventRepository>()
    singleOf(::EventDbService).bind<EventLocalDataSource>()
}