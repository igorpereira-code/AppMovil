package ucb.edu.bo.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ucb.edu.bo.dollar.domain.usecase.CreateDollarUseCase
import ucb.edu.bo.dollar.domain.usecase.GetDollarListUseCase
import ucb.edu.bo.events.domain.usecase.ClearEventsUseCase
import ucb.edu.bo.events.domain.usecase.CreateEventUseCase
import ucb.edu.bo.events.domain.usecase.GetEventListUseCase
import ucb.edu.bo.realtimedatabasecmp.domain.usecase.SaveTestDataUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.FetchRemoteConfigUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.GetRemoteStringUseCase

val domainModule = module {
    singleOf(::GetDollarListUseCase)
    singleOf(::CreateDollarUseCase)
    singleOf(::CreateDollarUseCase)
    factory { SaveTestDataUseCase(get()) }
    factory { FetchRemoteConfigUseCase(get()) }
    factory { GetRemoteStringUseCase(get()) }
    singleOf(::CreateEventUseCase)
    singleOf(::GetEventListUseCase)
    singleOf(::ClearEventsUseCase)
}