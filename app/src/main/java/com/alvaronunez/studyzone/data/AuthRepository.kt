package com.alvaronunez.studyzone.data

import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class AuthRepository {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() = mAuth.currentUser

    fun signOut() {
        mAuth.signOut()
    }

    fun thereIsUserSigned() = mAuth.currentUser != null

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult? =
        try {
            mAuth.signInWithEmailAndPassword(email, password).await()
        }catch (e: Exception){
            null
        }

    suspend fun signInWithCredential(token: String?): AuthResult? =
        try {
            val credential = GoogleAuthProvider.getCredential(token, null)
            mAuth.signInWithCredential(credential).await()
        }catch (e: Exception) {
            null
        }


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
