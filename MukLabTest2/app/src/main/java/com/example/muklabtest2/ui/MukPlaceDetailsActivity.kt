package com.example.muklabtest2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.muklabtest2.R
import com.example.muklabtest2.viewmodel.MukPlaceDetailsViewModel
import kotlinx.android.synthetic.main.activity_muk_place_details.*

class MukPlaceDetailsActivity : AppCompatActivity() {

    // Variables
    private var mukIsAdd = true
    private lateinit var mukPlaceDetailsViewModel: MukPlaceDetailsViewModel
    private var mukPlaceDetailsView: MukPlaceDetailsViewModel.MukPlaceDetailsView? = null
    private var mukDeleteItem: MenuItem? = null

    // Activity Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muk_place_details)

        mukSetupToolbar()
        mukSetupViewModel()
        mukSetupPlaceObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_place_details, menu)
        mukDeleteItem = menu?.findItem(R.id.mukDeleteItem)
        mukUpdateUI()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mukSaveItem -> {
                mukSavePlace()
                return true
            }
            R.id.mukDeleteItem -> {
                mukDeletePlace()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun mukSavePlace() {
        if (mukPlaceDetailsView == null) {
            mukPlaceDetailsView = mukPlaceDetailsViewModel.mukCreatePlaceView()
        }

        mukPlaceDetailsView?.let {
            if (mukValidateFields()) {
                it.mukTitle = mukTitleEditText.text.toString()
                it.mukSubtitle = mukSubtitleEditText.text.toString()
                it.mukLatitude = mukLatitudeEditText.text.toString()
                it.mukLongitude = mukLongitudeEditText.text.toString()

                var mukMessage = ""
                if (mukIsAdd) {
                    mukPlaceDetailsViewModel.mukAddPlace(it)
                    mukMessage = "Place Added"
                } else {
                    mukPlaceDetailsViewModel.mukUpdatePlace(it)
                    mukMessage = "Place Updated"
                }

                Toast.makeText(this, mukMessage, Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun mukDeletePlace() {
        mukPlaceDetailsView?.let {
            mukPlaceDetailsViewModel.mukDeletePlace(it)
            Toast.makeText(this, "Place Deleted", Toast.LENGTH_LONG).show()

            finish()
        }
    }

    // Utilities
    private fun mukSetupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun mukSetupViewModel() {
        mukPlaceDetailsViewModel = ViewModelProvider(this).get(MukPlaceDetailsViewModel::class.java)
    }

    private fun mukSetupPlaceObserver() {
        val mukId = intent.getLongExtra(MukMapPlacesActivity.MUK_PLACE_ID, 0)
        mukPlaceDetailsViewModel.mukGetPlaceView(mukId)?.observe(this) {
            it?.let {
                mukIsAdd = false
                mukPlaceDetailsView = it
                mukUpdateUI()
            }
        }
    }

    private fun mukUpdateUI() {
        if (mukIsAdd) {
            title = "Add Place"
            mukDeleteItem?.isVisible = false
        } else {
            title = "Edit Place"
        }

        mukPlaceDetailsView?.let {
            mukTitleEditText.setText(it.mukTitle)
            mukSubtitleEditText.setText(it.mukSubtitle)
            mukLatitudeEditText.setText(it.mukLatitude)
            mukLongitudeEditText.setText(it.mukLongitude)
        }
    }

    private fun mukValidateFields(): Boolean {
        var mukIsValid = true
        var mukMessage = ""

        val mukValidTitle = mukTitleEditText.text.toString()
        if (mukValidTitle.isEmpty()) {
            mukIsValid = false
            mukMessage = "Please Enter a Valid Title \n"
        }

        val mukValidSubTitle = mukSubtitleEditText.text.toString()
        if (mukValidSubTitle.isEmpty()) {
            mukIsValid = false
            mukMessage = "Please Enter a Valid Subtitle \n"
        }

        try {
            val mukLatitude = mukLatitudeEditText.text.toString().toDouble()
        } catch (e: Exception) {
            mukIsValid = false
            mukMessage += "Please Enter a Valid Latitude \n"
        }

        try {
            val mukLongitude = mukLongitudeEditText.text.toString().toDouble()
        } catch (e: Exception) {
            mukIsValid = false
            mukMessage += "Please Enter a Valid Longitude \n"
        }

        if (!mukIsValid) {
            mukDisplayAlert(mukMessage)
        }

        return mukIsValid
    }

    private fun mukDisplayAlert(mukMessage: String) {
        AlertDialog.Builder(this)
            .setTitle("Invalid Input")
            .setMessage(mukMessage)
            .setPositiveButton("Ok", null)
            .create()
            .show()
    }
}

/*
1. Lambton College: 43.7734, -79.3361
2. Parkside Square Apartments: 43.7744, -79.3336
3. Scotiabank: 43.7744, -79.3311
4. Food Basics: 43.7758, -79.3255
5. Audi Midtown: 43.7698, -79.3348
6. Tim Hortons: 43.7748, -79.3344
7. Burgers Park: 43.7703, -79.3324
 */