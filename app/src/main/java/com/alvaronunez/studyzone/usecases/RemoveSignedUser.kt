package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository

class RemoveSignedUser(private val repository: AuthenticationRepository) {

    suspend fun invoke() = repository.removeSignedUser()

}