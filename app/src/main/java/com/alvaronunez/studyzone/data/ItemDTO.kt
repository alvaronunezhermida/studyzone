package com.alvaronunez.studyzone.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class ItemDTO(
    @Exclude
    var uid: String? = null,
    var title: String? = null,
    var description: String? = null,
    var checklist: HashMap<String, Boolean>? = null,
    var categoryId: DocumentReference? = null,
    var userId: DocumentReference? = null

) {
    fun <T : ItemDTO?> withUid(uid: String): T {
        this.uid = uid
        return this as T
    }
}