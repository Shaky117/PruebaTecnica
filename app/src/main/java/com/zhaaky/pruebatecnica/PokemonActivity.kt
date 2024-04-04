package com.zhaaky.pruebatecnica

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhaaky.pruebatecnica.adapters.PokemonRVAdapter
import com.zhaaky.pruebatecnica.database.DatabaseBuilder
import com.zhaaky.pruebatecnica.database.entities.PokemonEntity

class PokemonActivity : AppCompatActivity() {
    lateinit var rvPokemon: RecyclerView
    lateinit var pokemonRVAdapter: PokemonRVAdapter
    lateinit var activityViewModel: PokemonViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)

        activityViewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)

        val db = DatabaseBuilder.getInstance(applicationContext)

        activityViewModel.setDatabase(db)

        rvPokemon = findViewById(R.id.rvPokemon)

        activityViewModel.getPokemonFromDB()

        activityViewModel.pokemonListed.observe(this, Observer { data ->
            // Update UI with data
            for (i in 0 until data.size) {
                activityViewModel.getPokemonDetails(data[i].url)
            }

            activityViewModel.getPokemonFromDB()
        })

        activityViewModel.pokemonDB.observe(this, Observer { data ->
            if (data != null) {
                if(data.size > 0){

                    setUpRV(data)
                }else{
                    activityViewModel.getPokemonList()
                }
            }
        })
    }

    private fun setUpRV(pokemonList: List<PokemonEntity>) {

        val layoutList = LinearLayoutManager(this@PokemonActivity)
        pokemonRVAdapter = PokemonRVAdapter(
            pokemonList,
            activityViewModel.getPokemonDao(),
            object : PokemonRVAdapter.OnItemClickListener {

                override fun onItemClick(pokemon: PokemonEntity) {
                    val intent = Intent(this@PokemonActivity, DetallesActivity::class.java)
                    intent.putExtra("Id", pokemon.id)
                    startActivity(intent)
                }
            })

        rvPokemon.apply {
            layoutManager = layoutList
            adapter = pokemonRVAdapter
        }

        var offset = 0;
        rvPokemon.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    offset += 20
                    activityViewModel.getMorePokemons(offset.toString())
                    rvPokemon.scrollToPosition(lastVisibleItemPosition)
                    Log.d("tag", "scrolled to end")
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.getPokemonFromDB()
    }
}
