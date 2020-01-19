package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.domain.User

class SignUpNewUser(private val repository: AuthenticationRepository) {

    suspend fun invoke(email: String, password: String, displayName: String): User? = repository.signUpNewUser(email, password, displayName)

}