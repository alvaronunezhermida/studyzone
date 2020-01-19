package com.alvaronunez.studyzone.domain

data class User(
    var id: String,
    var name: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    val displayName: String? = "$name $lastName"
)