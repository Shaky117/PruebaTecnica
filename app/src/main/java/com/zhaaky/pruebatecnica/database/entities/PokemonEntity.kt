package com.zhaaky.pruebatecnica.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey
    val id : Int,
    val name : String,
    val sprite : String?,
    val height : Int,
    val weight : Int,
    val favorite : Boolean = false
)
