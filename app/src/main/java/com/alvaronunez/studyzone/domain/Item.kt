package com.alvaronunez.studyzone.domain

data class Item(
    var uid: String? = null,
    var title: String? = null,
    var description: String? = null,
    var checklist: HashMap<String, Boolean>? = null,
    var categoryId: String? = null,
    var userId: String? = null
)

