package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.domain.Item

class GetItemsByUser(private val repository: Repository) {

    suspend fun invoke(userId: String): List<Item> = repository.getItemsByUser(userId)

}