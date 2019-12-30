package com.alvaronunez.studyzone.ui.common

import android.util.Patterns

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidName(name: String): Boolean {
    return name != ""
}

fun isValidPassword(password: String): Boolean {
    return password.length > 5
}

fun isValidConfirmedPassword(password: String, confirmedPassword: String): Boolean {
    return password == confirmedPassword
}