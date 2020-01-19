package com.alvaronunez.studyzone.data

import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class AuthRepository {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun thereIsUserSigned() = mAuth.currentUser != null


    suspend fun signUpNewUser(email: String, password: String, displayName: String): AuthResult? =
        try {
            val result = mAuth.createUserWithEmailAndPassword(email, password).await()
            val user = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            mAuth.currentUser?.updateProfile(user)
            result
        }catch (e: Exception) {
            null
        }

    fun removeCurrentUser() {
        mAuth.currentUser?.delete()
    }
}
