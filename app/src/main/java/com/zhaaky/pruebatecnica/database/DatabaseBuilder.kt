package com.zhaaky.pruebatecnica.database

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var INSTANCE: PokemonDatabase? = null

    fun getInstance(context: Context): PokemonDatabase {
        if (INSTANCE == null) {
            synchronized(PokemonDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
        return INSTANCE!!
    }
}
