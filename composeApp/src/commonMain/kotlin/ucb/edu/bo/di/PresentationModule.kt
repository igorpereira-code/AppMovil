package ucb.edu.bo.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ucb.edu.bo.config.presentation.viewmodel.SyncConfigViewModel
import ucb.edu.bo.dollar.presentation.viewmodel.DollarViewModel
import ucb.edu.bo.events.presentation.viewmodel.AppEventViewModel
import ucb.edu.bo.pushnotification.presentation.viewmodel.PushNotificationViewModel
import ucb.edu.bo.realtimedatabasecmp.presentation.viewmodel.FirebaseTestViewModel
import ucb.edu.bo.remoteconfig.presentation.viewmodel.RemoteConfigViewModel
import ucb.edu.bo.todoApp.calendar.presentation.viewmodel.CalendarViewModel
import ucb.edu.bo.todoApp.focus_mode.presentation.viewmodel.FocusViewModel
import ucb.edu.bo.todoApp.intro.presentation.viewmodel.IntroViewModel
import ucb.edu.bo.todoApp.login.presentation.viewmodel.LoginViewModel
import ucb.edu.bo.todoApp.login.presentation.viewmodel.RegisterViewModel
import ucb.edu.bo.todoApp.settings.presentation.viewmodel.SettingsViewModel
import ucb.edu.bo.todoApp.task.presentation.viewmodel.TaskViewModel

val presentationModule = module {
    singleOf(::DollarViewModel)
    viewModel { FirebaseTestViewModel(get()) }
    viewModel { PushNotificationViewModel() }
    viewModel { RemoteConfigViewModel(get(), get()) }
    viewModel { SyncConfigViewModel(get()) }
    viewModel { AppEventViewModel(get()) }
    viewModelOf(::IntroViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModel { FocusViewModel(get(), get()) }
    viewModelOf(::TaskViewModel)
    viewModelOf(::CalendarViewModel)
    viewModel { SettingsViewModel(settingsPreferences = get(),importGoogleCalendarUseCase = get(), quoteRepository = get())}
}