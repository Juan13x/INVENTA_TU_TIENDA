package com.juanguicj.inventa_tu_tienda.persistance.user

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class],  version = 1)
abstract class UserDataBase: RoomDatabase(){
    abstract fun userDAO(): UserDAO
}