package com.juanguicj.inventa_tu_tienda.persistance.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = true) val systemID: Int?,
    @ColumnInfo(name = "category") val category: String
)