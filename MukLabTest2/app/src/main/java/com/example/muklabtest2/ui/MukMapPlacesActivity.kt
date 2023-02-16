package com.example.muklabtest2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.muklabtest2.R
import com.example.muklabtest2.model.MukPlace
import com.example.muklabtest2.viewmodel.MukPlacesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_muk_map_places.*
import kotlin.math.max
import kotlin.math.min

class MukMapPlacesActivity : AppCompatActivity(), OnMapReadyCallback {

    // Variables
    private lateinit var mukPlacesViewModel: MukPlacesViewModel
    private lateinit var mukMap: GoogleMap

    // Activity Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muk_map_places)

        mukSetupToolbar()
        mukSetupMapFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_places, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mukAddItem) {
            mukGoToPlaceDetails(null)
        }

        return super.onOptionsItemSelected(item)
    }

    // OnMapReadyCallback Methods
    override fun onMapReady(mukGoogleMap: GoogleMap) {
        mukMap = mukGoogleMap

        mukSetupViewModel()
        mukSetupPlacesObserver()
        mukSetupMapListeners()
    }

    // Utilities
    private fun mukSetupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun mukSetupMapFragment() {
        val mukMapFragment = supportFragmentManager
            .findFragmentById(R.id.mukMap) as SupportMapFragment
        mukMapFragment.getMapAsync(this)
    }

    private fun mukSetupViewModel() {
        mukPlacesViewModel = ViewModelProvider(this).get(MukPlacesViewModel::class.java)
    }

    private fun mukSetupPlacesObserver() {
        mukPlacesViewModel.mukGetPlaceViews()?.observe(this) {
            it?.let {
                mukUpdateUI(it)
            }
        }
    }

    private fun mukUpdateUI(mukPlaceViews: List<MukPlacesViewModel.MukPlaceView>) {
        mukAddMarkers(mukPlaceViews)
        mukUpdateCamera(mukPlaceViews)
    }

    private fun mukAddMarkers(mukPlaceViews: List<MukPlacesViewModel.MukPlaceView>) {
        mukMap.clear()

        for (mukPlace in mukPlaceViews) {
            mukAddMarker(mukPlace)
        }
    }

    private fun mukAddMarker(mukPlaceView: MukPlacesViewModel.MukPlaceView) {
        val mukLatLng = LatLng(mukPlaceView.mukLatitude, mukPlaceView.mukLongitude)

        val mukMarker = mukMap.addMarker(MarkerOptions()
            .position(mukLatLng)
            .title(mukPlaceView.mukTitle)
            .snippet(mukPlaceView.mukSubTitle))

        mukMarker?.tag = mukPlaceView
    }

    private fun mukUpdateCamera(mukPlaceViews: List<MukPlacesViewModel.MukPlaceView>) {
        if (mukPlaceViews.isEmpty()) {
            return
        }

        var mukSouth = 90.0
        var mukWest = 180.0
        var mukNorth = -90.0
        var mukEast = -180.0

        for (mukPlaceView in mukPlaceViews) {
            mukSouth = min(mukSouth, mukPlaceView.mukLatitude)
            mukWest = min(mukWest, mukPlaceView.mukLongitude)
            mukNorth = max(mukNorth, mukPlaceView.mukLatitude)
            mukEast = max(mukEast, mukPlaceView.mukLongitude)
        }

        val mukSouthWest = LatLng(mukSouth, mukWest)
        val mukNorthEast = LatLng(mukNorth, mukEast)
        val mukBounds = LatLngBounds(mukSouthWest, mukNorthEast)

        val mukCameraUpdate = CameraUpdateFactory.newLatLngBounds(mukBounds, 50)
        mukMap.animateCamera(mukCameraUpdate)
    }

    private fun mukSetupMapListeners() {
        mukMap.setOnInfoWindowClickListener {
            val mukPlaceView = it.tag as? MukPlacesViewModel.MukPlaceView
            mukPlaceView?.mukId?.let { mukId ->
                mukGoToPlaceDetails(mukId)
            }
        }
    }

    private fun mukGoToPlaceDetails(mukId: Long?) {
        val mukIntent = Intent(this, MukPlaceDetailsActivity::class.java)

        mukId?.let {
            mukIntent.putExtra(MUK_PLACE_ID, it)
        }

        startActivity(mukIntent)
    }

    companion object {
        const val MUK_PLACE_ID = "MUK_PLACE_ID"
    }

}