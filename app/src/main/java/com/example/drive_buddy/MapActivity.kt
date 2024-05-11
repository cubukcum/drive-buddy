package com.example.drive_buddy

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import java.util.Locale

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        print("mape geldiniz")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        print("mape geldiniz")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        print("mape geldiniz2")
        mMap =googleMap
        mMap.setOnMapLongClickListener (dinleyici)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                println("enlem" + location.latitude)
                println("boylam" + location.longitude)
                mMap.clear()
                val guncelKonum =LatLng(location.latitude,location.longitude)
                mMap.addMarker(MarkerOptions().position(guncelKonum).title("guncel konumunuz"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(guncelKonum,15f))

                val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
                try {
                    val adresListesi=geocoder.getFromLocation(location.latitude,location.longitude,1)

                    if(adresListesi !=null){
                        if(adresListesi.size>0){
                            println(adresListesi.get(0).toString())

                        }
                    }

                }catch (e:Exception){
                    e.printStackTrace()
                }

            }
        }

        if (ContextCompat.checkSelfPermission(this@MapActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // İzin verilmemiş
            ActivityCompat.requestPermissions(this@MapActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            // İzin zaten verilmiş
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1f, locationListener)

            val sonBilinenKonum=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if(sonBilinenKonum != null){
                val sonBilinenLatLng=LatLng(sonBilinenKonum.latitude,sonBilinenKonum.longitude)
                mMap.addMarker(MarkerOptions().position(sonBilinenLatLng).title("guncel konumunuz"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sonBilinenLatLng,15f))
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzin verildi
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1f, locationListener)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    val dinleyici =object :GoogleMap.OnMapLongClickListener{
        override fun onMapLongClick(p0: LatLng) {
            mMap.clear()
            val geocoder=Geocoder(this@MapActivity,Locale.getDefault())
            if(p0 !=null){
                var adres = ""
                try {
                    val adresListesi=geocoder.getFromLocation(p0.latitude,p0.longitude,1)

                    if(adresListesi !=null){
                        if (adresListesi.size>0){

                            if(adresListesi.get(0).thoroughfare !=null){
                                adres +=adresListesi.get(0).thoroughfare
                            }


                            if(adresListesi.get(0).subThoroughfare !=null){
                                adres +=adresListesi.get(0).subThoroughfare
                            }

                        }
                    }

                    mMap.addMarker(MarkerOptions().position(p0).title(adres))

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }

    }
}