package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.domain.User

class SignInWithGoogleCredential(private val repository: AuthenticationRepository) {

    suspend fun invoke(token: String): User? = repository.signInWithCredential(token)

}