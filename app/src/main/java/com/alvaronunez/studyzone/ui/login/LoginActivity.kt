package com.alvaronunez.studyzone.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaronunez.studyzone.MainActivity
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.ui.signup.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity


class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

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
            googleSignIn()
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
                mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        loading.visibility = View.GONE
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            Toast.makeText(this, "${user?.displayName} logueado!", Toast.LENGTH_LONG).show()
                            startActivity<MainActivity>()
                            finish()
                        } else {
                            val message = when(task.exception){
                                is FirebaseAuthInvalidUserException -> "Tienes que registrarte primero"
                                is FirebaseAuthInvalidCredentialsException -> "Credenciales inválidas"
                                else -> "Login fallido!"
                            }
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show()
                // ...
            }
        }
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Toast.makeText(
            this, "Logueando en firebase",
            Toast.LENGTH_SHORT
        ).show()

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    Toast.makeText(this, "${user?.displayName} logueado con google!", Toast.LENGTH_LONG).show()
                    startActivity<MainActivity>()
                    finish()
                } else {
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
