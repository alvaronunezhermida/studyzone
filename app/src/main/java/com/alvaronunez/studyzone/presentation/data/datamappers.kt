package com.alvaronunez.studyzone.presentation.data

import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.presentation.data.database.ItemDb

fun ItemDb.toDomainItem() = Item(
    uid.toString(),
    title,
    description,
    null,
    categoryId,
    userId
)

fun Item.toRoomItem() = ItemDb(
    uid?.toInt(),
    title,
    description,
    categoryId,
    userId
)