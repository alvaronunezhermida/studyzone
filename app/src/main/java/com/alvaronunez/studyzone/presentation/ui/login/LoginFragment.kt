package com.alvaronunez.studyzone.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.ui.login.LoginViewModel.FormModel
import com.alvaronunez.studyzone.presentation.ui.login.LoginViewModel.UiModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by currentScope.viewModel(this)
    private lateinit var navController: NavController

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()

        setListeners()
        viewModelSetUp()
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
        viewModel.model.observe(this, Observer(::updateUi))
        viewModel.formModel.observe(this, Observer(::updateFormError))
    }

    private fun updateUi(model: UiModel) {
        loading.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is UiModel.Message -> Toast.makeText(this.context, model.message, Toast.LENGTH_LONG).show()
            is UiModel.NavigateToMain -> {
                navController.navigate(R.id.action_loginFragment_to_mainFragment)
            }
            is UiModel.NavigateToSignUp -> navController.navigate(R.id.action_loginFragment_to_signUpFragment)
            is UiModel.LoginWithGoogle -> startActivityForResult(model.intent, RC_SIGN_IN)
            is UiModel.Content -> viewModel.onLoginClicked(model.email, model.password)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            viewModel.fromGoogleSignInResult(getSignedAccountTokenFromIntent(data))
        }
    }

    private fun getSignedAccountTokenFromIntent(data: Intent?): String? {
        return try {
            GoogleSignIn.getSignedInAccountFromIntent(data).getResult(Exception::class.java)?.idToken
        } catch (e: Exception) {
            null
        }
    }

    private fun getGoogleSignInIntent(requestToken: String): Intent? {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(requestToken)
            .requestEmail()
            .build()
        this.context?.let {
            val googleSignInClient = GoogleSignIn.getClient(it, gso)
            return googleSignInClient.signInIntent
        }
        return null
    }

    private fun updateFormError(formModel: FormModel) {
        when(formModel){
            is FormModel.Email -> etUserEmail.error = formModel.error
            is FormModel.Password -> etPassword.error = formModel.error
        }
    }

}