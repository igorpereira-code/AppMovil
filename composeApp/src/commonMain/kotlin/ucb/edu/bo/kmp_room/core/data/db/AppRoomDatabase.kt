package ucb.edu.bo.kmp_room.core.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import ucb.edu.bo.config.data.dao.ConfigDao
import ucb.edu.bo.config.data.entity.ConfigEntity
import ucb.edu.bo.dollar.data.dao.DollarDao
import ucb.edu.bo.dollar.data.entity.DollarEntity
import ucb.edu.bo.events.data.dao.EventDao
import ucb.edu.bo.events.data.entity.EventEntity
import ucb.edu.bo.todoApp.task.data.dao.TaskDao
import ucb.edu.bo.todoApp.task.data.entity.TaskEntity
import ucb.edu.bo.remoteconfig.data.dao.RemoteConfigDao
import ucb.edu.bo.remoteconfig.data.entity.RemoteConfigEntity
import ucb.edu.bo.todoApp.category.data.dao.CategoryDao
import ucb.edu.bo.todoApp.category.data.entity.CategoryEntity

@Database(
    entities = [
        DollarEntity::class,
        TaskEntity::class,
        ConfigEntity::class,
        EventEntity::class,
        RemoteConfigEntity::class,
        CategoryEntity::class
    ],
    version = 7
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DollarDao
    abstract fun getConfigDao(): ConfigDao
    abstract fun getEventDao(): EventDao
    abstract fun taskDao(): TaskDao
    abstract fun remoteConfigDao(): RemoteConfigDao
    abstract fun categoryDao(): CategoryDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>