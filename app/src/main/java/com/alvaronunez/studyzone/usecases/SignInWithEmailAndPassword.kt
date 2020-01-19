package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.domain.User

class SignInWithEmailAndPassword(private val repository: AuthenticationRepository) {

    suspend fun invoke(email: String, password: String): User? = repository.signInWithEmailAndPassword(email, password)

}