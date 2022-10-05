package com.juanguicj.inventa_tu_tienda.persistance.products

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setProduct(product: Product)

    @Query("DELETE FROM product WHERE category = :categoryName AND code = :codeName")
    fun deleteProduct(categoryName: String, codeName: String)

    @Query("SELECT COUNT() FROM product WHERE category = :categoryName AND code = :codeName")
    fun isProductIncluded(categoryName: String, codeName: String):Int

    @Query("SELECT * FROM product WHERE category = :categoryName")
    fun getAllProducts(categoryName: String): List<Product>

    @Query("SELECT * FROM product WHERE category = :categoryName AND code = :codeName LIMIT 1")
    fun getProduct(categoryName: String, codeName: String): Product

}