package ucb.edu.bo.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ucb.edu.bo.dollar.presentation.viewmodel.DollarViewModel
import ucb.edu.bo.pushnotification.presentation.viewmodel.PushNotificationViewModel
import ucb.edu.bo.realtimedatabasecmp.presentation.viewmodel.FirebaseTestViewModel
import ucb.edu.bo.remoteconfig.presentation.viewmodel.RemoteConfigViewModel

val presentationModule = module {
    singleOf(::DollarViewModel)
    viewModel { FirebaseTestViewModel(get()) }
    viewModel { PushNotificationViewModel() }
    viewModel { RemoteConfigViewModel(get(), get()) }
}