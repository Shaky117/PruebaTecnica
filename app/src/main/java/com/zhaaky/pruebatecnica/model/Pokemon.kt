package com.zhaaky.pruebatecnica.model

import java.io.Serializable

data class PokemonListed(val results: ArrayList<PokemonList>)

data class PokemonList(
    var id : Int,
    var name: String,
    var url : String
): Serializable

data class Pokemon(
    var id: Int,
    val name: String,
    val sprite : String?,
    val height: Int,
    val weight: Int,
    val favorite : Boolean
)

data class Type(
    val name: String
)

data class Types(
    val slot : Int,
    val type : Type
)