package ucb.edu.bo.kmp_room.core.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import ucb.edu.bo.dollar.data.dao.DollarDao
import ucb.edu.bo.dollar.data.entity.DollarEntity
import ucb.edu.bo.formulario.data.dao.FormularioDao
import ucb.edu.bo.formulario.data.entity.FormularioEntity
import ucb.edu.bo.remoteconfig.data.dao.RemoteConfigDao
import ucb.edu.bo.remoteconfig.data.entity.RemoteConfigEntity
import ucb.edu.bo.todoApp.task.data.dao.TaskDao
import ucb.edu.bo.todoApp.task.data.entity.TaskEntity

@Database(
    entities = [
        DollarEntity::class,
        RemoteConfigEntity::class,
        FormularioEntity::class,
        TaskEntity::class
    ],
    version = 4
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DollarDao
    abstract fun getRemoteConfigDao(): RemoteConfigDao
    abstract fun getFormularioDao(): FormularioDao
    abstract fun taskDao(): TaskDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>
