package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.domain.Item

class AddItem(private val respository: Repository) {

    suspend fun invoke(item: Item): Boolean = respository.addItem(item)

}