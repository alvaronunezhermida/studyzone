package com.alvaronunez.studyzone.data.repository

import com.alvaronunez.studyzone.data.source.RemoteDataSource
import com.alvaronunez.studyzone.domain.Category
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.domain.User

class Repository(private val remoteDataSource: RemoteDataSource) {

    suspend fun getItemsByUser(userId: String): List<Item> = remoteDataSource.getItemsByUser(userId)

    suspend fun getCategoriesByUser(userId: String): List<Category> = remoteDataSource.getCategoriesByUser(userId)

    suspend fun addItem(item: Item): Boolean = remoteDataSource.addItem(item)

    suspend fun saveUser(user: User): Boolean = remoteDataSource.saveUser(user)
 }