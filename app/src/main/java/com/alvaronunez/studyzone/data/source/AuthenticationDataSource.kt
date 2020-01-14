package com.alvaronunez.studyzone.data.source

import com.alvaronunez.studyzone.domain.User

interface AuthenticationDataSource {
    fun getCurrentUser(): User
    fun signOut()
    fun thereIsUserSigned(): Boolean
    suspend fun signInWithEmailAndPassword(email: String, password: String): User
    suspend fun signInWithGoogleCredential(token: String): User
    suspend fun signUpNewUser(email: String, password: String, displayName: String): User
    fun removeCurrentUser()
}