package com.alvaronunez.studyzone.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaronunez.studyzone.MainActivity
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.GoogleSignInRepository
import com.alvaronunez.studyzone.ui.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity


class LoginActivity : AppCompatActivity() {

    private val authRepository : AuthRepository by lazy { AuthRepository() }
    private val googleSignInRepository : GoogleSignInRepository by lazy { GoogleSignInRepository() }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewSetUp()
    }

    private fun viewSetUp() {
        google_login.setOnClickListener {
            loading.visibility = View.VISIBLE
            startActivityForResult(googleSignInRepository.getGoogleSignInIntent(this, getString(R.string.default_web_client_id)), RC_SIGN_IN)
        }

        sign_up.setOnClickListener {
            startActivity<SignUpActivity>()
        }

        val focusListener = View.OnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) isFormValid()
        }

        userEmail.onFocusChangeListener = focusListener
        etPassword.onFocusChangeListener = focusListener


        login.setOnClickListener {
            if(isFormValid()) {
                val email: String = userEmail.text.toString()
                val pass: String = etPassword.text.toString()
                loading.visibility = View.VISIBLE
                authRepository.signInWithEmailAndPassword(email, pass){ result ->
                    loading.visibility = View.GONE
                    result.onSuccess {
                        val user = it?.user
                        Toast.makeText(this, "${user?.displayName} logueado!", Toast.LENGTH_LONG).show()
                        startActivity<MainActivity>()
                        finish()
                    }
                    result.onFailure {
                        Toast.makeText(this, "Login fallido!", Toast.LENGTH_LONG).show()
                        //TODO: Controlar estos dos casos
                        //SignInResult.INVALID_USER -> Toast.makeText(this, "Tienes que registrarte primero", Toast.LENGTH_LONG).show()
                        //SignInResult.INVALID_CREDENTIALS -> Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            googleSignInRepository.getSignedAccountTokenFromIntent(data)?.let{ token ->
                firebaseAuthWithGoogle(token)
            }?: run{
                loading.visibility = View.GONE
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun firebaseAuthWithGoogle(token: String) {
        authRepository.signInWithCredential(token) { result ->
            loading.visibility = View.GONE
            result.onSuccess {
                val user = authRepository.getCurrentUser()
                Toast.makeText(this, "${user?.displayName} logueado con google!", Toast.LENGTH_LONG)
                    .show()
                startActivity<MainActivity>()
                finish()
            }
            result.onFailure {
                Toast.makeText(this, "Error al logear con google!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length > 5
    }

    private fun isFormValid(): Boolean {
        if (!isValidEmail(userEmail.text.toString())) {
            userEmail.error = "Formato inválido"
            return false
        }
        if (!isValidPassword(etPassword.text.toString())) {
            etPassword.error = "Mínimo 6 caracteres"
            return false
        }
        return true
    }
}
