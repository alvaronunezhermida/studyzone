package com.alvaronunez.studyzone.data

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class CategoryDTO(
    @Exclude
    var uid: String? = null,
    var title: String? = null,
    var color: String? = null,
    var canChild: Boolean? = null

) {
    fun <T : CategoryDTO?> withUid(uid: String): T {
        this.uid = uid
        return this as T
    }
}