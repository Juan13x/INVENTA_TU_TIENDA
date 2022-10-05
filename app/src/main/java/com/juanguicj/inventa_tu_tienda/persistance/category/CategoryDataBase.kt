package com.juanguicj.inventa_tu_tienda.persistance.category

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Category::class], version = 1)
abstract class CategoryDataBase: RoomDatabase(){
    abstract fun categoryDAO() : CategoryDAO
}

