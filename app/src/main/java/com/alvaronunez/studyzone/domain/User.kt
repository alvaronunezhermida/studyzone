package com.alvaronunez.studyzone.domain

data class User(
    var id: String,
    var name: String,
    var lastName: String?,
    var email: String?
)