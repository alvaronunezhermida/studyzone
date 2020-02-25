package com.alvaronunez.studyzone.presentation.data.database

import androidx.room.*

@Dao
interface ItemDao {

    @Query("SELECT * FROM ItemDb WHERE userId = :userId")
    fun getItemsByUserId(userId: String): List<ItemDb>

    @Query("SELECT COUNT(uid) FROM ItemDb WHERE userId = :userId")
    fun itemCountByUserId(userId: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertItems(items: List<ItemDb>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addItem(item: ItemDb)


}