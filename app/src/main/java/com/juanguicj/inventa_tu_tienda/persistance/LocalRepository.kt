package com.juanguicj.inventa_tu_tienda.persistance

import android.app.Application
import androidx.room.Room
import com.juanguicj.inventa_tu_tienda.persistance.category.CategoryDataBase
import com.juanguicj.inventa_tu_tienda.persistance.products.ProductDataBase
import com.juanguicj.inventa_tu_tienda.persistance.user.UserDataBase

class LocalRepository: Application() {
    companion object{
        lateinit var user_db : UserDataBase
        lateinit var category_db: CategoryDataBase
        lateinit var product_db: ProductDataBase
    }

    override fun onCreate() {
        super.onCreate()
        user_db = Room.databaseBuilder(
            this,
            UserDataBase::class.java, "user-db"
        ).enableMultiInstanceInvalidation().build()
        category_db = Room.databaseBuilder(
            this,
            CategoryDataBase::class.java,
            "category-db"
        ).enableMultiInstanceInvalidation().build()
        product_db = Room.databaseBuilder(
            this,
            ProductDataBase::class.java,
            "product-db"
        ).enableMultiInstanceInvalidation().build()
    }
}