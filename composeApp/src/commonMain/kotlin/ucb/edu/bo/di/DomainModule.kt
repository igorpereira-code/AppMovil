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
}