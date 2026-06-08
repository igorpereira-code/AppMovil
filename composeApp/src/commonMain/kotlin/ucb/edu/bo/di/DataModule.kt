package ucb.edu.bo.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ucb.edu.bo.config.data.repository.ConfigRepositoryImpl
import ucb.edu.bo.config.domain.repository.ConfigRepository
import ucb.edu.bo.dollar.data.datasource.DollarLocalDataSource
import ucb.edu.bo.dollar.data.repository.DollarRepositoryImpl
import ucb.edu.bo.dollar.data.service.DbService
import ucb.edu.bo.dollar.domain.repository.DollarRepository
import ucb.edu.bo.events.data.repository.EventRepositoryImpl
import ucb.edu.bo.events.domain.repository.EventRepository
import ucb.edu.bo.firebase.RemoteConfigManager
import ucb.edu.bo.firebase.FirebaseManager
import ucb.edu.bo.formulario.data.datasource.FormularioFirebaseDataSource
import ucb.edu.bo.formulario.data.repository.FormularioRepositoryImpl
import ucb.edu.bo.formulario.domain.repository.FormularioRepository
import ucb.edu.bo.realtimedatabasecmp.data.datasource.FirebaseDataSource
import ucb.edu.bo.realtimedatabasecmp.data.repository.FirebaseTestRepositoryImpl
import ucb.edu.bo.realtimedatabasecmp.domain.repository.FirebaseTestRepository
import ucb.edu.bo.remoteconfig.data.repository.RemoteConfigRepositoryImpl
import ucb.edu.bo.remoteconfig.domain.repository.RemoteConfigRepository
import ucb.edu.bo.todoApp.focus_mode.data.datasource.FocusDataSource
import ucb.edu.bo.todoApp.focus_mode.data.repository.FocusRepositoryImpl
import ucb.edu.bo.todoApp.focus_mode.domain.repository.FocusRepository
import ucb.edu.bo.todoApp.login.data.datasource.AuthDataSource
import ucb.edu.bo.todoApp.login.data.repository.AuthRepositoryImpl
import ucb.edu.bo.todoApp.login.domain.repository.AuthRepository
import ucb.edu.bo.todoApp.task.data.datasource.TaskLocalDataSource
import ucb.edu.bo.todoApp.task.data.datasource.TaskLocalDataSourceImpl
import ucb.edu.bo.todoApp.task.data.repository.TaskRepositoryImpl
import ucb.edu.bo.todoApp.task.domain.repository.TaskRepository
import ucb.edu.bo.kmp_room.core.data.db.AppDatabase
import ucb.edu.bo.todoApp.settings.data.datasource.GoogleCalendarRemoteDataSource
import ucb.edu.bo.todoApp.settings.data.preferences.SettingsPreferencesImpl
import ucb.edu.bo.todoApp.settings.data.repository.CalendarAuthRepositoryImpl
import ucb.edu.bo.todoApp.settings.domain.preferences.ISettingsPreferences
import ucb.edu.bo.todoApp.settings.domain.repository.CalendarAuthRepository
import io.ktor.client.HttpClient
import ucb.edu.bo.remoteconfig.domain.repository.IRemoteConfigRepository
import ucb.edu.bo.todoApp.category.data.datasource.CategoryLocalDataSource
import ucb.edu.bo.todoApp.category.data.datasource.CategoryLocalDataSourceImpl
import ucb.edu.bo.todoApp.category.data.repository.CategoryRepositoryImpl
import ucb.edu.bo.todoApp.category.domain.repository.CategoryRepository
import ucb.edu.bo.todoApp.profile.data.datasource.ProfileDataSource
import ucb.edu.bo.todoApp.profile.data.repository.ProfileRepositoryImpl
import ucb.edu.bo.todoApp.profile.domain.repository.ProfileRepository

val dataModule = module {
    singleOf(::DollarRepositoryImpl).bind<DollarRepository>()
    singleOf(::DbService).bind<DollarLocalDataSource>()
    factory { FirebaseDataSource() }
    factory<FirebaseTestRepository> { FirebaseTestRepositoryImpl(get()) }
    single { RemoteConfigManager() }
    
    // Registro de FirebaseManager para que TaskSyncWorker pueda inyectarlo
    single { FirebaseManager() }

    single<RemoteConfigRepository> { RemoteConfigRepositoryImpl(get(), get()) }
    single<ConfigRepository>{ ConfigRepositoryImpl(get(), get()) }
    single<EventRepository> { EventRepositoryImpl(get()) }
    single { AuthDataSource() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { FocusDataSource() }
    single<FocusRepository> { FocusRepositoryImpl(get()) }

    // Task Module
    single { get<AppDatabase>().taskDao() }
    single { get<AppDatabase>().remoteConfigDao() }
    single<TaskLocalDataSource> { TaskLocalDataSourceImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<ISettingsPreferences> { SettingsPreferencesImpl(dataStore = get()) }

    single { get<AppDatabase>().categoryDao() }
    single<CategoryLocalDataSource> { CategoryLocalDataSourceImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }

    single { ProfileDataSource() }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }

    single { HttpClient() }
    single {GoogleCalendarRemoteDataSource(httpClient = get()) }
    single<CalendarAuthRepository> { CalendarAuthRepositoryImpl(remoteDataSource = get(), taskDao = get())}
}
