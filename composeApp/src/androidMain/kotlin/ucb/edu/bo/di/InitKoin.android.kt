package ucb.edu.bo.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.ktor.http.ContentDisposition.Companion.File
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ucb.edu.bo.formulario.data.preferences.FormularioPreferences
import ucb.edu.bo.formulario.data.preferences.IFormularioPreferences
import ucb.edu.bo.events.domain.usecase.BackgroundEventTrigger
import ucb.edu.bo.kmp_room.core.data.db.AppDatabase
import ucb.edu.bo.todoApp.settings.data.RetrofitQuoteRepositoryImpl
import ucb.edu.bo.todoApp.settings.domain.repository.QuoteRepository
import ucb.edu.bo.workmanager.AndroidBackgroundEventTrigger
import java.io.File

actual val platformModule = module {
    single<AppDatabase> {
        val context = androidContext()
        val dbFile = context.getDatabasePath("todo_app_db.db")
        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = {
                File(androidContext().filesDir, "datastore/settings.preferences_pb")
            }
        )
    }

    single<IFormularioPreferences> { FormularioPreferences(androidContext()) }
    single<BackgroundEventTrigger> { AndroidBackgroundEventTrigger(androidContext()) }
    single<QuoteRepository> { RetrofitQuoteRepositoryImpl() }

    // DAOs provided via AppDatabase
    single { get<AppDatabase>().getDao() }
    single { get<AppDatabase>().getConfigDao() }
    single { get<AppDatabase>().getEventDao() }

}