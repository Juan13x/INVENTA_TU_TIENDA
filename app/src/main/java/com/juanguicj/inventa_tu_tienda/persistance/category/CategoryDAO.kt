package com.juanguicj.inventa_tu_tienda.persistance.category

import androidx.room.*

@Dao
interface CategoryDAO {
    @Query("DELETE FROM category WHERE category = :categoryName")
    suspend fun deleteCategory(categoryName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setCategory(category: Category)

    @Query("SELECT * FROM category")
    suspend fun getCategories(): List<Category>

    @Query("SELECT COUNT() FROM category WHERE category = :categoryName")
    suspend fun isCategoryIncluded(categoryName: String) : Int
}