package ucb.edu.bo.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ucb.edu.bo.dollar.data.datasource.DollarLocalDataSource
import ucb.edu.bo.dollar.data.repository.DollarRepositoryImpl
import ucb.edu.bo.dollar.data.service.DbService
import ucb.edu.bo.dollar.domain.repository.DollarRepository
import ucb.edu.bo.firebase.RemoteConfigManager
import ucb.edu.bo.formulario.data.datasource.FormularioFirebaseDataSource
import ucb.edu.bo.formulario.data.repository.FormularioRepositoryImpl
import ucb.edu.bo.formulario.domain.repository.FormularioRepository
//import ucb.edu.bo.firebase.data.datasource.RemoteConfigManager
import ucb.edu.bo.realtimedatabasecmp.data.datasource.FirebaseDataSource
import ucb.edu.bo.realtimedatabasecmp.data.repository.FirebaseTestRepositoryImpl
import ucb.edu.bo.realtimedatabasecmp.domain.repository.FirebaseTestRepository
import ucb.edu.bo.remoteconfig.data.dao.RemoteConfigDao
import ucb.edu.bo.remoteconfig.data.repository.RemoteConfigRepositoryImpl
import ucb.edu.bo.remoteconfig.domain.repository.RemoteConfigRepository

val dataModule = module {
    singleOf(::DollarRepositoryImpl).bind<DollarRepository>()
    singleOf(::DbService).bind<DollarLocalDataSource>()
    factory { FirebaseDataSource() }
    factory<FirebaseTestRepository> { FirebaseTestRepositoryImpl(get()) }
    single { RemoteConfigManager() }
    single<RemoteConfigRepository> { RemoteConfigRepositoryImpl(get(), get()) }
    single { FormularioFirebaseDataSource() }
    single<FormularioRepository> { FormularioRepositoryImpl(get(), get()) }
}