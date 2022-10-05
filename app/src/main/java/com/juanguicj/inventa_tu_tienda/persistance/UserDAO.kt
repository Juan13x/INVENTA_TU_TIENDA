package com.juanguicj.inventa_tu_tienda.persistance

import androidx.room.*

@Dao
interface UserDAO {
    @Query("SELECT COUNT() FROM user WHERE systemID = :systemName")
    suspend fun checkTableCreated(systemName : String ): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setUser(user: User)

    @Query("SELECT * FROM user WHERE systemID = :systemName LIMIT 1")
    suspend fun getUser(systemName: String): User


}