package com.juanguicj.inventa_tu_tienda.persistance.products

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1)
abstract class ProductDataBase: RoomDatabase(){
    abstract fun productDAO() : ProductDAO
}