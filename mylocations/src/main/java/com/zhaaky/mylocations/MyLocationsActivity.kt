package com.zhaaky.mylocations

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MyLocationsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var locationManager: LocationManager
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val locationHandler = Handler(Looper.getMainLooper())
    private lateinit var mMap: GoogleMap
    private var locations = ArrayList<Locations>()

    companion object {
        const val LOCATION_REQUEST_INTERVAL = 20000L // 2 seconds
        const val REQUEST_CODE_LOCATION_PERMISSION = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION_PERMISSION)
        } else {
            startLocationUpdates()
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun displayLocationList(locations : List<Locations>){
        val rvList = findViewById<RecyclerView>(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(this)
        val rvAdapter = LocationAdapter(locations)
        rvList.adapter = rvAdapter
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationHandler.post(object : Runnable {
            override fun run() {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        sendLocationToFirebase(location)
                        val currentLatLng = LatLng(location.latitude, location.longitude)

                        locations.add(0, Locations(location.longitude, location.latitude, System.currentTimeMillis()))

                        displayLocationList(locations)

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                }, Looper.getMainLooper())

                locationHandler.postDelayed(this, LOCATION_REQUEST_INTERVAL)
            }
        })
    }

    private fun sendLocationToFirebase(location: Location) {
        val data = hashMapOf(
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "timeStamp" to System.currentTimeMillis()
        )

        firebaseFirestore.collection("locations").add(data)
            .addOnSuccessListener {
                Toast.makeText(this@MyLocationsActivity, "Ubicación enviada a Firebase: $location", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@MyLocationsActivity, "Error al enviar la ubicación a Firebase", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationHandler.removeCallbacksAndMessages(null)
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.isMyLocationEnabled = true
    }
}