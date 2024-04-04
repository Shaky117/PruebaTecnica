package com.zhaaky.pruebatecnica

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Activity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        val circularImageView = findViewById<CircleImageView>(R.id.ivProfile)
        val placeholder = ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground)

        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png" // URL de la imagen
        Picasso.get()
            .load(imageUrl)
            .placeholder(placeholder!!)
            .into(circularImageView)
    }
}