package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.domain.User

class SaveUser(private val respository: Repository) {

    suspend fun invoke(user: User): Boolean = respository.saveUser(user)

}