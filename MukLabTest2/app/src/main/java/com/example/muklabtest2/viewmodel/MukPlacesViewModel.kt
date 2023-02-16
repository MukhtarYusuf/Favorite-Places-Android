package com.example.muklabtest2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.muklabtest2.model.MukPlace
import com.example.muklabtest2.repository.MukPlaceRepo

class MukPlacesViewModel(application: Application): AndroidViewModel(application) {

    // Variables
    private var mukPlaceRepo = MukPlaceRepo(getApplication())
    private var mukPlaceViews: LiveData<List<MukPlaceView>>? = null

    // Methods
    fun mukGetPlaceViews(): LiveData<List<MukPlaceView>>? {
        if (mukPlaceViews == null) {
            mukMapPlacesToPlaceViews()
        }

        return mukPlaceViews
    }

    // Utilities
    private fun mukPlaceToPlaceView(mukPlace: MukPlace): MukPlaceView {
        return MukPlaceView(
            mukPlace.mukId,
            mukPlace.mukTitle,
            mukPlace.mukSubtitle,
            mukPlace.mukLatitude,
            mukPlace.mukLongitude
        )
    }

    private fun mukMapPlacesToPlaceViews() {
        mukPlaceViews = Transformations.map(mukPlaceRepo.mukGetAllLivePlaces()) { mukRepoPlaces ->
            mukRepoPlaces.map { mukPlace ->
                mukPlaceToPlaceView(mukPlace)
            }
        }
    }

    data class MukPlaceView(
        var mukId: Long? = null,
        var mukTitle: String = "",
        var mukSubTitle: String = "",
        var mukLatitude: Double = 0.0,
        var mukLongitude: Double = 0.0
    )
}