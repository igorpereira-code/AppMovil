package ucb.edu.bo.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ucb.edu.bo.events.domain.usecase.BackgroundEventTrigger
import ucb.edu.bo.kmp_room.core.data.db.AppDatabase
import ucb.edu.bo.workmanager.AndroidBackgroundEventTrigger

actual val platformModule = module {
    single<AppDatabase> {
        val context = androidContext()
        val dbFile = context.getDatabasePath("dollar_db.db")
        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    single { get<AppDatabase>().getDao() }
    single {get<AppDatabase>().getConfigDao()}

    single { get<AppDatabase>().getEventDao() }
    single<BackgroundEventTrigger> { AndroidBackgroundEventTrigger(androidContext()) }
}