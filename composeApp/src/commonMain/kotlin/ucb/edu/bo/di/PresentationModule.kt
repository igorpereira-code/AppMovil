package ucb.edu.bo.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ucb.edu.bo.dollar.presentation.viewmodel.DollarViewModel
import ucb.edu.bo.remoteconfig.MaintenanceViewModel

val presentationModule = module {
    singleOf(::DollarViewModel)
    viewModel { MaintenanceViewModel(get()) }
}