package com.juanguicj.inventa_tu_tienda.persistance

import android.app.Application
import androidx.room.Room

class LocalRepository: Application() {
    companion object{
        lateinit var user_db : UserDataBase
    }

    override fun onCreate() {
        super.onCreate()
        user_db = Room.databaseBuilder(
            this,
            UserDataBase::class.java, "user-db"
        ).enableMultiInstanceInvalidation().build()
    }
}