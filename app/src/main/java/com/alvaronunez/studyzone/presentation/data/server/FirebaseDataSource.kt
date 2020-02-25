package com.alvaronunez.studyzone.presentation.data.server

import com.alvaronunez.studyzone.data.CategoryDTO
import com.alvaronunez.studyzone.data.ItemDTO
import com.alvaronunez.studyzone.data.source.RemoteDataSource
import com.alvaronunez.studyzone.domain.Category
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.domain.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseDataSource: RemoteDataSource {

    private val db = FirebaseFirestore.getInstance()

    override suspend fun getItemsByUser(userId: String): List<Item> =
        try {
            val userRef = db.document("users/$userId")
            val documents = db.collection("items").whereEqualTo("userId", userRef).get().await()
            documents.toObjects(ItemDTO::class.java).map(ItemDTO::toItem)
        }catch (e: Exception) {
            emptyList()
        }

    override suspend fun getCategoriesByUser(userId: String): List<Category> =
        try {
            val userRef = db.document("users/$userId")
            val documents = db.collection("categories").whereIn("userId", mutableListOf(userRef, "all")).get().await()
            documents.toObjects(CategoryDTO::class.java).map(CategoryDTO::toCategory)
        }catch (e: Exception) {
            emptyList()
        }

    override suspend fun addItem(item: Item): Boolean =
        try {
            db.collection("items").add(hashMapOf(
                "title" to item.title,
                "description" to item.description,
                "userId" to db.document("users/${item.userId}"))
            ).await()
            true
        } catch (e: Exception) {
            false
        }

    override suspend fun saveUser(user: User): Boolean =
        try {
            db.collection("users").document(user.id).set(user).await()
            true
        } catch (e: Exception) {
            false
        }

}

fun ItemDTO.toItem() = Item(uid, title, description, checklist, categoryId?.id, userId?.id)

fun CategoryDTO.toCategory() = Category(uid, title, color, canChild)