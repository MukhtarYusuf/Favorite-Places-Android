package com.example.muklabtest2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.muklabtest2.db.MukPlaceDao
import com.example.muklabtest2.model.MukPlace
import com.example.muklabtest2.repository.MukPlaceRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MukPlaceDetailsViewModel(application: Application): AndroidViewModel(application) {

    // Variables
    private var mukPlaceRepo = MukPlaceRepo(getApplication())
    private var mukPlaceDetailsView: LiveData<MukPlaceDetailsView>? = null

    // Methods
    fun mukGetPlaceView(mukId: Long): LiveData<MukPlaceDetailsView>? {
        if (mukPlaceDetailsView == null) {
            mukMapPlaceToPlaceDetailsView(mukId)
        }

        return mukPlaceDetailsView
    }

    fun mukAddPlace(mukPlaceDetailsView: MukPlaceDetailsView) {
        val mukPlace = mukPlaceRepo.mukCreatePlace()
        mukPlace.mukTitle = mukPlaceDetailsView.mukTitle
        mukPlace.mukSubtitle = mukPlaceDetailsView.mukSubtitle
        mukPlace.mukLatitude = mukPlaceDetailsView.mukLatitude.toDouble()
        mukPlace.mukLongitude = mukPlaceDetailsView.mukLongitude.toDouble()

        GlobalScope.launch {
            mukPlaceRepo.mukAddPlace(mukPlace)
        }
    }

    fun mukUpdatePlace(mukPlaceDetailsView: MukPlaceDetailsView) {
        GlobalScope.launch {
            val mukPlace = mukPlaceDetailsViewToPlace(mukPlaceDetailsView)
            mukPlace?.let {
                mukPlaceRepo.mukUpdatePlace(it)
            }
        }
    }

    fun mukDeletePlace(mukPlaceDetailsView: MukPlaceDetailsView) {
        GlobalScope.launch {
            val mukPlace = mukPlaceDetailsViewToPlace(mukPlaceDetailsView)
            mukPlace?.let {
                mukPlaceRepo.mukDeletePlace(it)
            }
        }
    }

    fun mukCreatePlaceView(): MukPlaceDetailsView {
        return MukPlaceDetailsView()
    }

    // Utilities
    private fun mukPlaceToPlaceDetailsView(mukPlace: MukPlace): MukPlaceDetailsView {
        return MukPlaceDetailsView(
            mukPlace.mukId,
            mukPlace.mukTitle,
            mukPlace.mukSubtitle,
            "${mukPlace.mukLatitude}",
            "${mukPlace.mukLongitude}"
        )
    }

    private fun mukPlaceDetailsViewToPlace(mukPlaceDetailsView: MukPlaceDetailsView): MukPlace? {
        val mukPlace = mukPlaceDetailsView.mukId?.let {
            mukPlaceRepo.mukGetPlace(it)
        }

        mukPlace?.let {
            it.mukId = mukPlaceDetailsView.mukId
            it.mukTitle = mukPlaceDetailsView.mukTitle
            it.mukSubtitle = mukPlaceDetailsView.mukSubtitle
            it.mukLatitude = mukPlaceDetailsView.mukLatitude.toDouble()
            it.mukLongitude = mukPlaceDetailsView.mukLongitude.toDouble()
        }

        return mukPlace
    }

    private fun mukMapPlaceToPlaceDetailsView(mukId: Long) {
        val mukPlace = mukPlaceRepo.mukGetLivePlace(mukId)
        mukPlaceDetailsView = Transformations.map(mukPlace) {
            it?.let {
                mukPlaceToPlaceDetailsView(it)
            }
        }
    }

    data class MukPlaceDetailsView(
        var mukId: Long? = null,
        var mukTitle: String = "",
        var mukSubtitle: String = "",
        var mukLatitude: String = "",
        var mukLongitude: String = ""
    )
}