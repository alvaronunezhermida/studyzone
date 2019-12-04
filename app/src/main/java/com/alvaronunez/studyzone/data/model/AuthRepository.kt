package com.alvaronunez.studyzone.data.model

import android.util.Log
import com.google.firebase.auth.*

class AuthRepository {

    companion object {
        private const val LOG_TAG = "AUTH_REPO::"
    }

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() = mAuth.currentUser

    fun signOut() {
        mAuth.signOut()
    }

    fun thereIsUserSigned() = mAuth.currentUser != null

    fun signInWithEmailAndPassword(email: String, password: String, callback: (SignInResult) -> Unit) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(SignInResult.SUCCESS)
            else {
                when (task.exception) {
                    is FirebaseAuthInvalidUserException -> callback(SignInResult.INVALID_USER)
                    is FirebaseAuthInvalidCredentialsException -> callback(SignInResult.INVALID_CREDENTIALS)
                    else -> callback(SignInResult.FAILED)
                }
            }
        }
    }

    fun signInWithCredential(token: String?, success: (Boolean) -> Unit) {
        if(!token.isNullOrBlank()) {
            val credential = GoogleAuthProvider.getCredential(token, null)
            mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if(!task.isSuccessful) Log.e(LOG_TAG, task.exception?.message?: "Exception lost")
                success(task.isSuccessful)
            }
        }else {
            success(false)
        }

    }

    fun signUpNewUser(email: String, password: String, displayName: String, callback: (CreateUserResult) -> Unit) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                mAuth.currentUser?.updateProfile(user)?.addOnCompleteListener { updateTask ->
                    if(updateTask.isSuccessful) callback(CreateUserResult.SUCCESS)
                    else {
                        Log.e(LOG_TAG, task.exception?.message?: "Exception lost")
                        callback(CreateUserResult.FAILED)
                    }
                }?: run{
                    Log.e(LOG_TAG, "User lost when update after sign up")
                    callback(CreateUserResult.FAILED)
                }
            } else {
                if (task.exception?.message == "The email address is badly formatted.") callback(CreateUserResult.EMAIL_BADLY_FORMATTED)
                else {
                    Log.e(LOG_TAG, task.exception?.message?: "Exception lost")
                    callback(CreateUserResult.FAILED)
                }
            }
        }

    }
}

enum class SignInResult {
    SUCCESS, INVALID_USER, INVALID_CREDENTIALS, FAILED
}

enum class CreateUserResult {
    SUCCESS, FAILED, EMAIL_BADLY_FORMATTED
}