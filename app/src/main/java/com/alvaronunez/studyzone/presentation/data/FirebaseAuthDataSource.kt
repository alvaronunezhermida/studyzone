package com.alvaronunez.studyzone.presentation.data

import com.alvaronunez.studyzone.data.source.AuthenticationDataSource
import com.alvaronunez.studyzone.domain.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirebaseAuthDataSource : AuthenticationDataSource {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun getSignedUser(): User? {
        mAuth.currentUser?.let {
            return User(it.uid, displayName = it.displayName)
        }?: run{
            return null
        }
    }

    override fun signOut() {
        mAuth.signOut()
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): User? =
        try {
            val authResult = mAuth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let {
                User(it.uid, displayName = it.displayName, email = it.email)
            }
        }catch (e: Exception){
            null
        }

    override suspend fun signInWithGoogleCredential(token: String): User? =
        try {
            val credential = GoogleAuthProvider.getCredential(token, null)
            val authResult = mAuth.signInWithCredential(credential).await()
            authResult.user?.let {
                User(it.uid, displayName = it.displayName, email = it.email)
            }
        }catch (e: Exception) {
            null
        }

    override suspend fun signUpNewUser(email: String, password: String, displayName: String): User? =
        try {
            val authResult = mAuth.createUserWithEmailAndPassword(email, password).await()
            val user = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            mAuth.currentUser?.updateProfile(user)
            authResult.user?.let {
                User(it.uid, displayName = it.displayName, email = it.email)
            }
        }catch (e: Exception) {
            null
        }

    override fun removeSignedUser() { mAuth.currentUser?.delete() }

}