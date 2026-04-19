package ucb.edu.bo.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ucb.edu.bo.dollar.data.datasource.DollarLocalDataSource
import ucb.edu.bo.dollar.data.repository.DollarRepositoryImpl
import ucb.edu.bo.dollar.data.service.DbService
import ucb.edu.bo.dollar.domain.repository.DollarRepository
import ucb.edu.bo.remoteconfig.MaintenanceViewModel
import ucb.edu.bo.remoteconfig.RemoteConfigRepository

val dataModule = module {
    singleOf(::DollarRepositoryImpl).bind<DollarRepository>()
    singleOf(::DbService).bind<DollarLocalDataSource>()
    single { RemoteConfigRepository() }
}

