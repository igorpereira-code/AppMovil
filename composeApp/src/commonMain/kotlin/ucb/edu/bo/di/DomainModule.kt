package ucb.edu.bo.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ucb.edu.bo.config.domain.usecase.SyncInitialConfigUseCase
import ucb.edu.bo.dollar.domain.usecase.CreateDollarUseCase
import ucb.edu.bo.dollar.domain.usecase.GetDollarListUseCase
import ucb.edu.bo.events.domain.usecase.LogAndSyncAppEventUseCase
import ucb.edu.bo.realtimedatabasecmp.domain.usecase.SaveTestDataUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.FetchRemoteConfigUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.GetCachedConfigUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.GetRemoteStringUseCase
import ucb.edu.bo.remoteconfig.domain.usecase.SyncRemoteConfigUseCase
import ucb.edu.bo.todoApp.category.domain.usecase.CreateCategoryUseCase
import ucb.edu.bo.todoApp.category.domain.usecase.GetAllCategoriesUseCase
import ucb.edu.bo.todoApp.focus_mode.domain.usecase.GetWeekSessionsUseCase
import ucb.edu.bo.todoApp.focus_mode.domain.usecase.SaveFocusSessionUseCase
import ucb.edu.bo.todoApp.login.domain.usecase.LoginUseCase
import ucb.edu.bo.todoApp.login.domain.usecase.RegisterUseCase
import ucb.edu.bo.todoApp.profile.domain.usecase.GetProfileUseCase
import ucb.edu.bo.todoApp.profile.domain.usecase.LogoutUseCase
import ucb.edu.bo.todoApp.profile.domain.usecase.UpdateNameUseCase
import ucb.edu.bo.todoApp.profile.domain.usecase.UpdatePasswordUseCase
import ucb.edu.bo.todoApp.settings.domain.usecase.ImportGoogleCalendarUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.CreateTaskUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.DeleteTaskUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.GetAllTasksUseCase
import ucb.edu.bo.todoApp.task.domain.usecase.ToggleTaskUseCase

val domainModule = module {
    singleOf(::GetDollarListUseCase)
    singleOf(::CreateDollarUseCase)
    singleOf(::CreateDollarUseCase)
    factory { SaveTestDataUseCase(get()) }
    factory { FetchRemoteConfigUseCase(get()) }
    factory { GetRemoteStringUseCase(get()) }
    factory{ SyncInitialConfigUseCase(get()) }
    factory { LogAndSyncAppEventUseCase(get()) }
    factory { SyncRemoteConfigUseCase(get()) }
    factory { GetCachedConfigUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { SaveFocusSessionUseCase(get()) }
    factory { GetWeekSessionsUseCase(get()) }

    // Task Use Cases
    factory { GetAllTasksUseCase(get()) }
    factory { CreateTaskUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }
    factory { ToggleTaskUseCase(get()) }
    factory {ImportGoogleCalendarUseCase(repository = get())}
    factory { GetAllCategoriesUseCase(get()) }
    factory { CreateCategoryUseCase(get()) }

    factory { GetProfileUseCase(get()) }
    factory { UpdateNameUseCase(get()) }
    factory { UpdatePasswordUseCase(get()) }
    factory { LogoutUseCase(get()) }
}
