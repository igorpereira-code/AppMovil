package ucb.edu.bo.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ucb.edu.bo.dollar.presentation.viewmodel.DollarViewModel

val presentationModule = module {
    singleOf(::DollarViewModel)
}