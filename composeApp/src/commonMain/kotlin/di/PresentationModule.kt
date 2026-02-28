package di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import koin.presentation.viewModel.ProductDetailViewModel

val presentationModule = module {
    viewModelOf(
        ::ProductDetailViewModel)
}
