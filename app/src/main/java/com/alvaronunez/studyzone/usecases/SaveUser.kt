package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.domain.User

class SaveUser(private val repository: Repository) {

    suspend fun invoke(user: User): Boolean = repository.saveUser(user)

}