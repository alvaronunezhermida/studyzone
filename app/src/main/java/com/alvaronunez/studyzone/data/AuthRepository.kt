package com.alvaronunez.studyzone.data

import com.google.firebase.auth.*
import java.lang.Exception

class AuthRepository {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() = mAuth.currentUser

    fun signOut() {
        mAuth.signOut()
    }

    fun thereIsUserSigned() = mAuth.currentUser != null

    fun signInWithEmailAndPassword(email: String, password: String, callback: (Result<AuthResult?>) -> Unit) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) callback(Result.success(task.result))
            else {
                when (task.exception) {
                    is FirebaseAuthInvalidUserException -> callback(Result.failure(task.exception?: Exception())) //TODO: Espicificar error para usuario inválido
                    is FirebaseAuthInvalidCredentialsException -> callback(Result.failure(task.exception?: Exception())) //TODO: Especificar error para credenciales inválidas
                    else -> callback(Result.failure(task.exception?: Exception()))
                }
            }
        }
    }

    fun signInWithCredential(token: String?, callback: (Result<AuthResult?>) -> Unit) {
        try {
            val credential = GoogleAuthProvider.getCredential(token, null)
            mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) callback(Result.success(task.result))
                else callback(Result.failure(task.exception ?: Exception()))
            }
        }catch (e: Exception) {
            callback(Result.failure(e))
        }

    }

    fun signUpNewUser(email: String, password: String, displayName: String, callback: (Result<AuthResult>) -> Unit) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { result ->
            try {
                val user = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build()
                mAuth.currentUser?.updateProfile(user)?.addOnSuccessListener {
                    callback(Result.success(result))
                }?.addOnFailureListener { e ->
                    callback(Result.failure(e))
                }
            }catch (e: Exception) {
                callback(Result.failure(e))
            }
        }.addOnFailureListener { e ->
            //if (e.message == "The email address is badly formatted.") callback(Result.failure(e))//TODO: Especificar error formato email incorrecto
            callback(Result.failure(e))
        }

    }

    fun removeCurrentUser() {
        mAuth.currentUser?.delete()
    }
}
