package com.alvaronunez.studyzone.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaronunez.studyzone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewSetUp()
    }

    private fun viewSetUp() {
        login.setOnClickListener {
            val email: String = username.text.toString()
            val pass: String = password.text.toString()

            if(email != ""  && pass != "") {
                mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            Toast.makeText(this, "${user?.displayName} logueado!", Toast.LENGTH_LONG).show()
                        } else {
                            val message = when(task.exception){
                                is FirebaseAuthInvalidUserException -> {
                                    signUp(email, pass)
                                    "registrando"
                                }
                                is FirebaseAuthInvalidCredentialsException -> "Credenciales inválidas"
                                else -> "Login fallido!"
                            }
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
    }

    private fun signUp(email: String, pass: String) {
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    Toast.makeText(this, "${user?.displayName} registrado!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
