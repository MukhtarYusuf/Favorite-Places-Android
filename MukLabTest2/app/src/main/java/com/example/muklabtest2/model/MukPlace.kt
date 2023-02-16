package com.example.muklabtest2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MukPlace(
        @PrimaryKey(autoGenerate = true) var mukId: Long? = null,
        var mukTitle: String = "",
        var mukSubtitle: String = "",
        var mukLatitude: Double = 0.0,
        var mukLongitude: Double = 0.0
)

