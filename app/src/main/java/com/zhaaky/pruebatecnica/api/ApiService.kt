package com.zhaaky.pruebatecnica.api

import com.zhaaky.pruebatecnica.model.Pokemon
import com.zhaaky.pruebatecnica.model.PokemonList
import com.zhaaky.pruebatecnica.model.PokemonListed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("pokemon")
    fun getPokemonList() : Call<PokemonListed>

    @GET
    fun getPokemonDetail(@Url url: String) : Call<Pokemon>

    @GET("pokemon/")
    fun getMorePokemon(@Query("offset") nextPage: String) : Call<PokemonListed>
}