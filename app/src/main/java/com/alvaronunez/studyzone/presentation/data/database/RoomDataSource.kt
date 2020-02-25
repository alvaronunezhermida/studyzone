package com.alvaronunez.studyzone.presentation.data.database

import com.alvaronunez.studyzone.data.source.LocalDataSource
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.presentation.data.toDomainItem
import com.alvaronunez.studyzone.presentation.data.toRoomItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(db: ItemDatabase) : LocalDataSource {

    private val itemDao = db.itemDao()

    override suspend fun isEmpty(userId: String): Boolean =
        withContext(Dispatchers.IO) {
            itemDao.itemCountByUserId(userId) <= 0
        }

    override suspend fun getItemsById(userId: String): List<Item> =
        withContext(Dispatchers.IO) {
            itemDao.getItemsByUserId(userId).map { it.toDomainItem() }
        }

    override suspend fun saveItems(items: List<Item>) =
        withContext(Dispatchers.IO) {
            itemDao.insertItems(items.map { it.toRoomItem() })
        }

    override suspend fun addItem(item: Item) =
        withContext(Dispatchers.IO) {
            itemDao.addItem(item.toRoomItem())
        }

}