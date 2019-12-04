package com.alvaronunez.studyzone.ui.signup

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaronunez.studyzone.MainActivity
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.CreateUserResult
import com.alvaronunez.studyzone.data.model.DatabaseRepository
import com.alvaronunez.studyzone.data.model.UserDTO
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.startActivity


class SignUpActivity : AppCompatActivity() {

    private val authRepository : AuthRepository by lazy { AuthRepository() }
    private val databaseRepository : DatabaseRepository by lazy { DatabaseRepository() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sign_up.setOnClickListener {
            if (isFormValid()){
                val email = userEmail.text.toString()
                val pass = etPassword.text.toString()
                signUp(email, pass)
            }
        }

        val focusListener = View.OnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) isFormValid()
        }

        userName.onFocusChangeListener = focusListener
        userEmail.onFocusChangeListener = focusListener
        etPassword.onFocusChangeListener = focusListener
        etConfirmPassword.onFocusChangeListener = focusListener
    }

    private fun signUp(email: String, pass: String) {
        val username = userName.text.toString()
        val lastName = lastName.text.toString()
        var failed = false
        loading.visibility = View.VISIBLE
        authRepository.signUpNewUser(email, pass, "$username $lastName") { result ->
            when (result) {
                CreateUserResult.SUCCESS -> {
                    authRepository.getCurrentUser()?.let { currentUser ->
                        databaseRepository.saveUserDB(
                            currentUser.uid,
                            UserDTO(username, lastName, email)
                        ) { result ->
                            if (result) {
                                loading.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "${currentUser.displayName} registrado!",
                                    Toast.LENGTH_LONG
                                ).show()
                                startActivity<MainActivity>()
                                finish()
                            } else {
                                failed = true
                            }
                        }
                    }?: run {
                        failed = true
                    }
                }
                CreateUserResult.FAILED -> failed = true
                CreateUserResult.EMAIL_BADLY_FORMATTED -> {
                    userEmail.error = "Formato inválido"
                    failed = true
                }
            }
            if (failed) {
                loading.visibility = View.GONE
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidName(name: String): Boolean {
        return name != ""
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length > 5
    }

    private fun isValidConfirmedPassword(password: String, confirmedPassword: String): Boolean {
        return password == confirmedPassword
    }

    private fun isFormValid(): Boolean {
        if (!isValidName(userName.text.toString())) {
            userName.error = "Campo vacío"
            return false
        }
        if (!isValidEmail(userEmail.text.toString())) {
            userEmail.error = "Formato inválido"
            return false
        }
        if(!isValidPassword(etPassword.text.toString())){
            etPassword.error = "Mínimo 6 caracteres"
            return false
        }
        if (!isValidConfirmedPassword(etPassword.text.toString(), etConfirmPassword.text.toString())){
            etConfirmPassword.error = "No coincide con la contraseña"
            return false
        }
        return true
    }
}
