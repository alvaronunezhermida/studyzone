package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository

class SignOutSignedUser(private val repository: AuthenticationRepository) {

    suspend fun invoke() = repository.signOut()

}