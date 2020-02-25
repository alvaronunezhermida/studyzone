package com.alvaronunez.studyzone.data.repository

import com.alvaronunez.studyzone.data.source.LocalDataSource
import com.alvaronunez.studyzone.data.source.RemoteDataSource
import com.alvaronunez.studyzone.domain.Category
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.domain.User

class Repository(private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource) {

    suspend fun getItemsByUser(userId: String): List<Item> {
        if(localDataSource.isEmpty(userId)) {
            val items = remoteDataSource.getItemsByUser(userId)
            localDataSource.saveItems(items)
        }
        return localDataSource.getItemsById(userId)
    }

    suspend fun getCategoriesByUser(userId: String): List<Category> = remoteDataSource.getCategoriesByUser(userId)

    suspend fun addItem(item: Item): Boolean {
        localDataSource.addItem(item)
        return remoteDataSource.addItem(item)
    }

    suspend fun saveUser(user: User): Boolean = remoteDataSource.saveUser(user)

 }