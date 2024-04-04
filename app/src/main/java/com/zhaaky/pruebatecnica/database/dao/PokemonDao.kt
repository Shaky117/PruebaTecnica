package com.zhaaky.pruebatecnica.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zhaaky.pruebatecnica.database.entities.PokemonEntity
import com.zhaaky.pruebatecnica.model.Pokemon

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon")
    fun getAllPokemon() : List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun getDetailsFromPokemonWithId(id : Int) : Pokemon

    @Query("UPDATE pokemon SET favorite = :fav WHERE id = :id")
    fun updateFavPokemon(id : Int, fav: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemonEntity: PokemonEntity)
}