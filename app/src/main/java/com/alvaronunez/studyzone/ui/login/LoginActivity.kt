package com.alvaronunez.studyzone.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaronunez.studyzone.MainActivity
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.ui.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity


class LoginActivity : AppCompatActivity(), LoginPresenter.View {

    private val presenter by lazy { LoginPresenter() }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter.onCreate(this)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            presenter.fromGoogleSignInResult(data)
        }
    }

    private fun isFormValid(): Boolean {
        if (!presenter.isValidEmail(userEmail.text.toString())) {
            userEmail.error = "Formato inválido"
            return false
        }
        if (!presenter.isValidPassword(etPassword.text.toString())) {
            etPassword.error = "Mínimo 6 caracteres"
            return false
        }
        return true
    }

    override fun setListeners() {
        google_login.setOnClickListener {
            presenter.onGoogleLoginClicked(this)
        }

        sign_up.setOnClickListener {
            presenter.onSignUpClicked()
        }

        login.setOnClickListener {
            if(isFormValid()) presenter.onLoginClicked(userEmail.text.toString(), etPassword.text.toString())
        }

        val focusListener = View.OnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) isFormValid()
        }

        userEmail.onFocusChangeListener = focusListener
        etPassword.onFocusChangeListener = focusListener
    }

    override fun showProgress() {
        loading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        loading.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun loginWithGoogle(googleIntent: Intent) {
        startActivityForResult(googleIntent, RC_SIGN_IN)
    }

    override fun navigateToSignUp() {
        startActivity<SignUpActivity>()
    }

    override fun navigateToMain() {
        startActivity<MainActivity>()
        finish()
    }
}
