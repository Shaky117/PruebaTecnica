package com.zhaaky.pruebatecnica.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "types")
data class Types(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val name : String
)
