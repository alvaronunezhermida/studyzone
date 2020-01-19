package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.domain.User

class GetSignedUser(private val repository: AuthenticationRepository) {

    suspend fun invoke(): User? = repository.getSignedUser()

}