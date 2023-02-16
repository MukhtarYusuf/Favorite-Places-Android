package com.example.muklabtest2.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.muklabtest2.model.MukPlace

@Dao
interface MukPlaceDao {
    @Query("SELECT * FROM MukPlace")
    fun mukLoadAll(): LiveData<List<MukPlace>>

    @Query("SELECT * FROM MukPlace WHERE mukId = :mukPlaceId")
    fun mukLoadLivePlace(mukPlaceId: Long): LiveData<MukPlace>

    @Query("SELECT * FROM MukPlace WHERE mukId = :mukPlaceId")
    fun mukLoadPlace(mukPlaceId: Long): MukPlace

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun mukInsertPlace(mukPlace: MukPlace): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun mukUpdatePlace(mukPlace: MukPlace)

    @Delete
    fun mukDeletePlace(mukPlace: MukPlace)
}