package com.juanguicj.inventa_tu_tienda.persistance.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val systemID: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "category") val category: String
)