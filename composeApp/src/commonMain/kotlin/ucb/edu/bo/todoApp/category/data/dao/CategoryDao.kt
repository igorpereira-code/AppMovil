package ucb.edu.bo.todoApp.category.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ucb.edu.bo.todoApp.category.data.entity.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    suspend fun getAll(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoryEntity)
}