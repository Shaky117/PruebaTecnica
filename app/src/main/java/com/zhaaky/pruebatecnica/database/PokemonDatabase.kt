package com.zhaaky.pruebatecnica.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zhaaky.pruebatecnica.database.dao.PokemonDao
import com.zhaaky.pruebatecnica.database.entities.PokemonEntity
import com.zhaaky.pruebatecnica.database.entities.Types

@Database(entities = [PokemonEntity::class, Types::class], version = 4, exportSchema = true)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao() : PokemonDao
}