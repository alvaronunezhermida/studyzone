package com.alvaronunez.studyzone.data.source

import com.alvaronunez.studyzone.domain.Item


interface LocalDataSource {
    suspend fun isEmpty(userId: String): Boolean
    suspend fun getItemsById(userId: String): List<Item>
    suspend fun saveItems(items: List<Item>)
    suspend fun addItem(item: Item)
}