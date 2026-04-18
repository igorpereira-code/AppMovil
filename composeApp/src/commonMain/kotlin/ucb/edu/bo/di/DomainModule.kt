package ucb.edu.bo.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ucb.edu.bo.dollar.domain.usecase.CreateDollarUseCase
import ucb.edu.bo.dollar.domain.usecase.GetDollarListUseCase

val domainModule = module {
    singleOf(::GetDollarListUseCase)
    singleOf(::CreateDollarUseCase)
}