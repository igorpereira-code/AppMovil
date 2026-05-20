package ucb.edu.bo.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ucb.edu.bo.dollar.presentation.viewmodel.DollarViewModel
import ucb.edu.bo.formulario.presentation.viewmodel.FormularioViewModel
import ucb.edu.bo.pushnotification.presentation.viewmodel.PushNotificationViewModel
import ucb.edu.bo.realtimedatabasecmp.presentation.viewmodel.FirebaseTestViewModel
import ucb.edu.bo.remoteconfig.presentation.viewmodel.RemoteConfigViewModel
import ucb.edu.bo.todoApp.focus_mode.presentation.viewmodel.FocusViewModel
import ucb.edu.bo.todoApp.intro.presentation.viewmodel.IntroViewModel
import ucb.edu.bo.todoApp.login.presentation.viewmodel.LoginViewModel
import ucb.edu.bo.todoApp.login.presentation.viewmodel.RegisterViewModel

val presentationModule = module {
    viewModelOf(::DollarViewModel)
    viewModel { FirebaseTestViewModel(get()) }
    viewModel { PushNotificationViewModel() }
    viewModel { RemoteConfigViewModel(get(), get()) }
    viewModel { FormularioViewModel(get(), get(), get(), get()) }
    viewModelOf(::IntroViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModel { FocusViewModel(get(), get()) }
}