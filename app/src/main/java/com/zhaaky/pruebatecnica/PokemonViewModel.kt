package com.zhaaky.pruebatecnica


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhaaky.pruebatecnica.api.ApiService
import com.zhaaky.pruebatecnica.api.RetrofitClient
import com.zhaaky.pruebatecnica.database.PokemonDatabase
import com.zhaaky.pruebatecnica.database.dao.PokemonDao
import com.zhaaky.pruebatecnica.database.entities.PokemonEntity
import com.zhaaky.pruebatecnica.model.Pokemon
import com.zhaaky.pruebatecnica.model.PokemonList
import com.zhaaky.pruebatecnica.model.PokemonListed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonViewModel : ViewModel() {

    val pokemonListed = MutableLiveData<ArrayList<PokemonList>>()
    val pokemonDetails = MutableLiveData<Pokemon>()
    val pokemonDB = MutableLiveData<List<PokemonEntity>>()

    private lateinit var pokemonDatabase: PokemonDatabase
    private lateinit var pokemonDao : PokemonDao
    private val apiService = RetrofitClient.getClient()?.create(ApiService::class.java)

    fun getPokemonList() {
        apiService?.getPokemonList()?.enqueue(object : Callback<PokemonListed> {
            override fun onResponse(call: Call<PokemonListed>, response: Response<PokemonListed>) {
                if (response.isSuccessful) {
                    pokemonListed.value = response.body()!!.results

                } else {
                    // Handle unsuccessful responses
                }
            }

            override fun onFailure(call: Call<PokemonListed>, t: Throwable) {
                // Handle failure
            }
        })
    }

    fun getPokemonDetails(url : String){
        apiService?.getPokemonDetail(url)?.enqueue(object : Callback<Pokemon>{
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.isSuccessful){
                    pokemonDetails.value = response.body()!!

                    if(pokemonDetails.value != null) {
                        val pokemonEntity = PokemonEntity(
                            id = pokemonDetails.value!!.id,
                            name = pokemonDetails.value!!.name,
                            height = pokemonDetails.value!!.height,
                            weight = pokemonDetails.value!!.weight,
                            sprite = pokemonDetails.value!!.sprite
                        )

                        insertPokemonIntoDB(pokemonEntity)
                    }
                }
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                //Handle Failure
                Log.d("pokemons Details", "something went wrong")
            }
        })
    }

    fun getMorePokemons(offset: String) {
        apiService?.getMorePokemon(offset)?.enqueue(object : Callback<PokemonListed> {
            override fun onResponse(call: Call<PokemonListed>, response: Response<PokemonListed>) {
                if (response.isSuccessful) {
                    pokemonListed.value = response.body()!!.results

                } else {
                    // Handle unsuccessful responses
                }
            }

            override fun onFailure(call: Call<PokemonListed>, t: Throwable) {
                // Handle failure
            }
        })
    }

    fun setDatabase(db : PokemonDatabase){
        pokemonDatabase = db
        pokemonDao = pokemonDatabase.pokemonDao()
    }

    fun getPokemonDao() : PokemonDao{
        return pokemonDao
    }

    private fun insertPokemonIntoDB(pokemon : PokemonEntity){
        pokemonDao = pokemonDatabase.pokemonDao()
        GlobalScope.launch(Dispatchers.IO) {
            pokemonDao.insertPokemon(pokemon)
        }
    }

    fun getPokemonDetailsFromDB(id : Int){
        GlobalScope.launch(Dispatchers.IO) {
            pokemonDetails.postValue(pokemonDao.getDetailsFromPokemonWithId(id))
        }
    }

    fun getPokemonFromDB() {
        pokemonDao = pokemonDatabase.pokemonDao()
        GlobalScope.launch(Dispatchers.IO) {
            pokemonDB.postValue(pokemonDao.getAllPokemon())
        }
    }

    fun updateFav(id: Int, fav: Boolean){
        GlobalScope.launch(Dispatchers.IO) {
            pokemonDao.updateFavPokemon(id, fav)
        }
    }
}