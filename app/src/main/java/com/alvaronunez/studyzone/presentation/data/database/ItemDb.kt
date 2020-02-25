package com.alvaronunez.studyzone.presentation.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemDb(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    var title: String?,
    var description: String?,
    var categoryId: String?,
    var userId: String?
)