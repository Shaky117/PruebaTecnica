package com.zhaaky.pruebatecnica

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.zhaaky.pruebatecnica.database.DatabaseBuilder
import com.zhaaky.pruebatecnica.model.Pokemon
import de.hdodenhof.circleimageview.CircleImageView

class DetallesActivity : AppCompatActivity() {

    private lateinit var activityViewModel: PokemonViewModel

    private val imageURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles)

        val btnBack = findViewById<ImageView>(R.id.btnBack)


        val bundle = intent.extras

        val id = bundle!!.getInt("Id")

        activityViewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)

        val db = DatabaseBuilder.getInstance(applicationContext)

        activityViewModel.setDatabase(db)
        activityViewModel.getPokemonDetailsFromDB(id)

        activityViewModel.pokemonDetails.observe(this, Observer { data ->
            // Update UI with data
            setUpViews(data)
        })

        btnBack.setOnClickListener { finish() }

    }

    private fun updateFavIcon(btnFav : ImageButton, fav : Boolean){
        btnFav.setImageResource(if (fav) R.drawable.heart_yes else R.drawable.heart_no)
    }

    private fun setUpViews(pokemon : Pokemon) {

        val nombre = findViewById<TextView>(R.id.tvNombre)
        val imagen = findViewById<CircleImageView>(R.id.ivPokemon)
        val weight = findViewById<TextView>(R.id.tvWeight)
        val height = findViewById<TextView>(R.id.tvHeight)
        val btnFav = findViewById<ImageButton>(R.id.btnFavorite)

        var fav = pokemon.favorite

        Picasso.get().load(imageURL + pokemon.id + ".png").into(imagen)
        nombre.text = pokemon.name + " #" + pokemon.id
        weight.text = pokemon.weight.toString()
        height.text = pokemon.height.toString()

        updateFavIcon(btnFav, fav)

        btnFav.setOnClickListener{
            fav = !fav
            updateFavIcon(btnFav, fav)

            activityViewModel.updateFav(pokemon.id, fav)
        }
    }
}