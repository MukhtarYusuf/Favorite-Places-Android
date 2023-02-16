package com.example.muklabtest2.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.muklabtest2.db.MukLabTest2Database
import com.example.muklabtest2.model.MukPlace

class MukPlaceRepo(var context: Context) {

    // Variables
    private var mukDb = MukLabTest2Database.mukGetInstance(context)
    private var mukPlaceDao = mukDb.mukPlaceDao()

    // Methods
    fun mukGetAllLivePlaces(): LiveData<List<MukPlace>> {
        return mukPlaceDao.mukLoadAll()
    }

    fun mukGetLivePlace(mukPlaceId: Long): LiveData<MukPlace> {
        return mukPlaceDao.mukLoadLivePlace(mukPlaceId)
    }

    fun mukGetPlace(mukPlaceId: Long): MukPlace {
        return mukPlaceDao.mukLoadPlace(mukPlaceId)
    }

    fun mukAddPlace(mukPlace: MukPlace): Long {
        val mukId = mukPlaceDao.mukInsertPlace(mukPlace)
        mukPlace.mukId = mukId

        return mukId
    }

    fun mukUpdatePlace(mukPlace: MukPlace) {
        mukPlaceDao.mukUpdatePlace(mukPlace)
    }

    fun mukDeletePlace(mukPlace: MukPlace) {
        mukPlaceDao.mukDeletePlace(mukPlace)
    }

    fun mukCreatePlace(): MukPlace {
        return MukPlace()
    }

}