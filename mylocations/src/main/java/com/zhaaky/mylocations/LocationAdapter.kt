package com.zhaaky.mylocations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LocationAdapter (val locations: List<Locations>): RecyclerView.Adapter<LocationAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gps, parent, false)
        return LocationAdapterViewHolder(view, parent.context)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: LocationAdapterViewHolder, position: Int) {
        val pokemon = locations[position]
        return holder.bind(pokemon)
    }
}

class LocationAdapterViewHolder(itemView: View, context: Context): RecyclerView.ViewHolder(itemView) {
    private val tvLong: TextView = itemView.findViewById(R.id.tvLon)
    private val tvLat : TextView = itemView.findViewById(R.id.tvLat)
    private val tvDate : TextView = itemView.findViewById(R.id.tvTime)

    fun bind(location: Locations) {
        tvLat.text = "Latitude: ${location.lat}"
        tvLong.text = "Longitude: ${location.long}"

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = location.timeStamp
        val date = calendar.time

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formatedDate = simpleDateFormat.format(date)

        tvDate.text = "TimeStamp: ${formatedDate}"
    }
}