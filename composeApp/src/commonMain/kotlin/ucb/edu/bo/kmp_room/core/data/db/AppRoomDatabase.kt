package ucb.edu.bo.kmp_room.core.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import ucb.edu.bo.dollar.data.dao.DollarDao
import ucb.edu.bo.dollar.data.entity.DollarEntity
import ucb.edu.bo.events.data.dao.EventDao
import ucb.edu.bo.events.data.entity.EventEntity // 1. IMPORTANTE: Importar la entidad

// 2. AÑADIR EventEntity a la lista y SUBIR la versión a 2
@Database(entities = [DollarEntity::class, EventEntity::class], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DollarDao
    abstract fun getEventDao(): EventDao
}

// El resto del archivo se mantiene igual
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>