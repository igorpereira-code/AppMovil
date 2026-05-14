package ucb.edu.bo.kmp_room.core.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import ucb.edu.bo.dollar.data.dao.DollarDao
import ucb.edu.bo.dollar.data.entity.DollarEntity
import ucb.edu.bo.app.home.data.local.dao.TaskDao
import ucb.edu.bo.app.home.data.local.entity.TaskEntity

@Database(entities = [DollarEntity::class, TaskEntity::class], version = 3)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DollarDao
    abstract fun getTaskDao(): TaskDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>