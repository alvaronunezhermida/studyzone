package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.domain.Category

class GetCategoriesByUser(private val repository: Repository) {

    suspend fun invoke(userId: String): List<Category> = repository.getCategoriesByUser(userId)

}