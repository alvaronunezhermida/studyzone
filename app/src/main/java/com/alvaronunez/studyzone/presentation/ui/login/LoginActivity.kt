package com.alvaronunez.studyzone.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alvaronunez.studyzone.presentation.ui.login.LoginViewModel.UiModel
import com.alvaronunez.studyzone.presentation.ui.login.LoginViewModel.FormModel
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.ui.main.MainActivity
import com.alvaronunez.studyzone.presentation.ui.signup.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import java.lang.Exception


class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setListeners()
        viewModelSetUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            viewModel.fromGoogleSignInResult(getSignedAccountTokenFromIntent(data))
        }
    }

  
    private fun setListeners() {
        google_login.setOnClickListener {
            viewModel.onGoogleLoginClicked(getGoogleSignInIntent(getString(R.string.default_web_client_id)))
        }

        sign_up.setOnClickListener {
            viewModel.onSignUpClicked()
        }

        login.setOnClickListener {
            viewModel.isFormValid(etUserEmail.text.toString(), etPassword.text.toString())
        }
    }

    private fun viewModelSetUp() {
        viewModel = ViewModelProviders.of(
            this,
            LoginViewModelFactory())[LoginViewModel::class.java]

        viewModel.model.observe(this, Observer(::updateUi))
        viewModel.formModel.observe(this, Observer(::updateFormError))
    }

    private fun updateUi(model: UiModel) {
        loading.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is UiModel.Message -> Toast.makeText(this, model.message, Toast.LENGTH_LONG).show()
            is UiModel.NavigateToMain -> {
                startActivity<MainActivity>()
                finish()
            }
            is UiModel.NavigateToSignUp -> startActivity<SignUpActivity>()
            is UiModel.LoginWithGoogle -> startActivityForResult(model.intent, RC_SIGN_IN)
            is UiModel.Content -> viewModel.onLoginClicked(model.email, model.password)
        }
    }

    private fun getSignedAccountTokenFromIntent(data: Intent?): String? {
        return try {
            GoogleSignIn.getSignedInAccountFromIntent(data).getResult(Exception::class.java)?.idToken
        } catch (e: Exception) {
            null
        }
    }

    private fun getGoogleSignInIntent(requestToken: String): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(requestToken)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        return googleSignInClient.signInIntent
    }

    private fun updateFormError(formModel: FormModel) {
        when(formModel){
            is FormModel.Email -> etUserEmail.error = formModel.error
            is FormModel.Password -> etPassword.error = formModel.error
        }
    }
}
