package com.alvaronunez.studyzone.presentation.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ItemDb::class], version = 1)
abstract class ItemDatabase : RoomDatabase() {

    companion object {
        fun build(context: Context) = Room.databaseBuilder(
            context,
            ItemDatabase::class.java,
            "item-db"
        ).build()
    }

    abstract fun itemDao(): ItemDao

}