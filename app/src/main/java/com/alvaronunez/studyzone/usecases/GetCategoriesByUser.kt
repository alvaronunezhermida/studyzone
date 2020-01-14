package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.domain.Category

class GetCategoriesByUser(private val respository: Repository) {

    suspend fun invoke(userId: String): List<Category> = respository.getCategoriesByUser(userId)

}