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

@Database(
    entities = [
        DollarEntity::class,
        RemoteConfigEntity::class,
        FormularioEntity::class
    ],
    version = 3
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): DollarDao
    abstract fun getRemoteConfigDao(): RemoteConfigDao
    abstract fun getFormularioDao(): FormularioDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(ctx: Any? = null): RoomDatabase.Builder<AppDatabase>