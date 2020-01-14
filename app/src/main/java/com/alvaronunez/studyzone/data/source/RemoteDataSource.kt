package com.alvaronunez.studyzone.data.source

import com.alvaronunez.studyzone.domain.Category
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.domain.User

interface RemoteDataSource {
    suspend fun getItemsByUser(userId: String): List<Item>
    suspend fun getCategoriesByUser(userId: String): List<Category>
    suspend fun addItem(item: Item): Boolean
    suspend fun saveUser(user: User): Boolean
}