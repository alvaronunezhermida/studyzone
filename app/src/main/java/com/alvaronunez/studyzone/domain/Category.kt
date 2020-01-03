package com.alvaronunez.studyzone.domain

data class Category(
    var uid: String? = null,
    var title: String? = null,
    var color: String? = null,
    var canChild: Boolean? = null

)