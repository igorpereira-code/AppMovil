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

@Database(entities = [DollarEntity::class, ConfigEntity::class, EventEntity::class], version =3)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DollarDao
    abstract fun getConfigDao(): ConfigDao

    abstract fun getEventDao(): EventDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>