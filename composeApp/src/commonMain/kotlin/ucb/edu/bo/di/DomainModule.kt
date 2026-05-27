package ucb.edu.bo.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ucb.edu.bo.dollar.domain.usecase.CreateDollarUseCase
import ucb.edu.bo.dollar.domain.usecase.GetDollarListUseCase
import ucb.edu.bo.formulario.domain.usecase.GetLatestFormularioUseCase
import ucb.edu.bo.formulario.domain.usecase.SaveFormularioLocalUseCase
import ucb.edu.bo.formulario.domain.usecase.SyncFormularioUseCase
import ucb.edu.bo.realtimedatabasecmp.domain.usecase.SaveTestDataUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.FetchRemoteConfigUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.GetCachedConfigUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.GetRemoteStringUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.SyncRemoteConfigUseCase
import ucb.edu.bo.todoApp.focus_mode.domain.usecase.GetWeekSessionsUseCase
import ucb.edu.bo.todoApp.focus_mode.domain.usecase.SaveFocusSessionUseCase
import ucb.edu.bo.todoApp.intro.domain.usecase.GetIntroPagesUseCase
import ucb.edu.bo.todoApp.intro.domain.usecase.IsOnboardingCompletedUseCase
import ucb.edu.bo.todoApp.intro.domain.usecase.MarkOnboardingCompletedUseCase
import ucb.edu.bo.todoApp.login.domain.usecase.LoginUseCase
import ucb.edu.bo.todoApp.login.domain.usecase.RegisterUseCase

val domainModule = module {
    singleOf(::GetDollarListUseCase)
    singleOf(::CreateDollarUseCase)
    factory { SaveTestDataUseCase(get()) }
    factory { FetchRemoteConfigUseCase(get()) }
    factory { GetRemoteStringUseCase(get()) }
    factory { SyncRemoteConfigUseCase(get()) }
    factory { GetCachedConfigUseCase(get()) }
    factory { SaveFormularioLocalUseCase(get()) }
    factory { GetLatestFormularioUseCase(get()) }
    factory { SyncFormularioUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { SaveFocusSessionUseCase(get()) }
    factory { GetWeekSessionsUseCase(get()) }
    //////
    factory { GetIntroPagesUseCase(get()) }
    factory { IsOnboardingCompletedUseCase(get()) }
    factory { MarkOnboardingCompletedUseCase(get()) }
}