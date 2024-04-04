package com.zhaaky.pruebatecnica.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zhaaky.pruebatecnica.R
import com.zhaaky.pruebatecnica.database.dao.PokemonDao
import com.zhaaky.pruebatecnica.database.entities.PokemonEntity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale

class PokemonRVAdapter (val pokemonList: List<PokemonEntity>, val pokemonDao: PokemonDao ,val listener: OnItemClickListener): RecyclerView.Adapter<PokemonRVAdapterViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(pokemon: PokemonEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonRVAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonRVAdapterViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: PokemonRVAdapterViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        return holder.bind(pokemon, this, listener)
    }

    fun updateFav(id: Int, fav: Boolean){
       GlobalScope.launch(Dispatchers.IO) {
           pokemonDao.updateFavPokemon(id, fav)
       }
    }
}

class PokemonRVAdapterViewHolder(itemView: View, context: Context): RecyclerView.ViewHolder(itemView) {
    private val foto: CircleImageView = itemView.findViewById(R.id.ivPokemon)
    private val nombre: TextView = itemView.findViewById(R.id.tvNombre)
    private val btnFav : ImageButton = itemView.findViewById(R.id.btnFavorite)
    val imageURL = context.applicationContext.getString(R.string.image_url)
    fun bind(pokemon: PokemonEntity,adapter : PokemonRVAdapter,listener: PokemonRVAdapter.OnItemClickListener) {
        Picasso.get().load(imageURL + pokemon.id + ".png").into(foto)
        nombre.text = pokemon.name.uppercase(Locale.ROOT)
        var fav = pokemon.favorite

        btnFav.setImageResource(if (fav) R.drawable.heart_yes else R.drawable.heart_no)

        itemView.setOnClickListener { listener.onItemClick(pokemon) }
        btnFav.setOnClickListener {
            fav = !fav
            adapter.updateFav(pokemon.id, fav)
            btnFav.setImageResource(if (fav) R.drawable.heart_yes else R.drawable.heart_no)
        }
    }
}