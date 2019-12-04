package com.alvaronunez.studyzone.data.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseRepository {

    companion object {
        private const val LOG_TAG = "DB_REPO::"
    }

    private val db = FirebaseFirestore.getInstance()

    fun saveUserDB(userUID: String, user: UserDTO, callback: (Boolean) -> Unit) {
        db.collection("users")
            .document(userUID)
            .set(user)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
                if(!task.isSuccessful) Log.e(LOG_TAG, task.exception?.message?: "")
            }

    }
}