package com.alvaronunez.studyzone.data.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseRepository {

    companion object {
        private const val LOG_TAG = "DB_REPO::"
    }

    private val db = FirebaseFirestore.getInstance()

    fun saveUserDB(userUID: String, user: UserDTO, callback: (Result<Void?>) -> Unit) {
        db.collection("users")
            .document(userUID)
            .set(user)
            .addOnCompleteListener { task ->
                callback(Result.success(task.result))
                if(!task.isSuccessful) Log.e(LOG_TAG, task.exception?.message?: "")
            }

    }

    fun getItemsByUser(userId: String?, callback: (Result<List<ItemDTO>>) -> Unit) {
        val userRef = db.document("users/$userId")
        db.collection("items")
            .whereEqualTo("userId", userRef)
            .get()
            .addOnSuccessListener { documents ->
                val items: MutableList<ItemDTO> = mutableListOf()
                documents.forEach { document ->
                    items.add(document.toObject(ItemDTO::class.java).withUid(document.id))
                }
                callback(Result.success(items))
            }
            .addOnFailureListener { exception ->
                callback(Result.failure(exception))
            }
    }
}