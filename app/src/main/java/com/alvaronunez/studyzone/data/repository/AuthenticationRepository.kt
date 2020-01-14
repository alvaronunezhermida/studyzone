package com.alvaronunez.studyzone.data.repository

import com.alvaronunez.studyzone.data.source.AuthenticationDataSource

class AuthenticationRepository (private val authenticationDataSource: AuthenticationDataSource) {
    fun getCurrentUser() = authenticationDataSource.getCurrentUser()
    fun signOut() = authenticationDataSource.signOut()
    fun thereIsUserSigned() = authenticationDataSource.thereIsUserSigned()
    suspend fun signInWithEmailAndPassword(email: String, password: String) = authenticationDataSource.signInWithEmailAndPassword(email, password)
    suspend fun signInWithCredential(token: String) = authenticationDataSource.signInWithGoogleCredential(token)
    suspend fun signUpNewUser(email: String, password: String, displayName: String) = authenticationDataSource.signUpNewUser(email, password, displayName)
    fun removeCurrentUser() = authenticationDataSource.removeCurrentUser()
}