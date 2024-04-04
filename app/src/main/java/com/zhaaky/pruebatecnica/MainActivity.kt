package com.zhaaky.pruebatecnica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zhaaky.mylocations.MyLocationsActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnActivity1 = findViewById<Button>(R.id.btnActivity1)
        val btnPokemon = findViewById<Button>(R.id.btnPokemon)
        val btnLocations = findViewById<Button>(R.id.btnMyLocations)

        btnActivity1.setOnClickListener{
            val intent = Intent(this@MainActivity, Activity1::class.java)
            startActivity(intent)
        }

        btnPokemon.setOnClickListener{
            val intent = Intent(this@MainActivity, PokemonActivity::class.java)
            startActivity(intent)
        }

        btnLocations.setOnClickListener {
            val intent = Intent(this@MainActivity, MyLocationsActivity::class.java)
            startActivity(intent)
        }
    }
}