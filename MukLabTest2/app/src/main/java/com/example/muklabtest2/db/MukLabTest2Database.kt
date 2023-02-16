package com.example.muklabtest2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.muklabtest2.model.MukPlace

@Database(entities = [MukPlace::class], version = 1)
abstract class MukLabTest2Database: RoomDatabase() {
    abstract fun mukPlaceDao(): MukPlaceDao

    companion object {
        private var mukInstance: MukLabTest2Database? = null

        fun mukGetInstance(context: Context): MukLabTest2Database {
            if (mukInstance == null) {
                mukInstance = Room.databaseBuilder(context.applicationContext,
                MukLabTest2Database::class.java,
                "MukLabTest2")
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return mukInstance as MukLabTest2Database
        }
    }
}