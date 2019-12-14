package com.alvaronunez.studyzone.data.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

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

    suspend fun getItemsByUser(userId: String?): List<ItemDTO>? =
        try {
            val userRef = db.document("users/$userId")
            val documents = db.collection("items").whereEqualTo("userId", userRef).get().await()
            documents.toObjects(ItemDTO::class.java)
        }catch (e: Exception) {
            null
        }

}