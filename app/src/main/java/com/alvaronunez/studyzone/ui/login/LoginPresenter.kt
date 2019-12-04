package com.alvaronunez.studyzone.ui.login

import android.content.Intent
import android.util.Patterns
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.GoogleSignInRepository

class LoginPresenter {

    interface View {
        fun setListeners()
        fun showProgress()
        fun hideProgress()
        fun showMessage(message: String)
        fun loginWithGoogle(googleIntent: Intent)
        fun navigateToSignUp()
        fun navigateToMain()
    }

    private val authRepository : AuthRepository by lazy { AuthRepository() }
    private val googleSignInRepository : GoogleSignInRepository by lazy { GoogleSignInRepository() }
    private var view: View? = null

    fun onCreate(view: View) {
        this.view = view
        view.setListeners()
    }


    fun onSignUpClicked() {
        view?.navigateToSignUp()
    }

    fun onLoginClicked(email: String, password: String) {
        view?.showProgress()
        authRepository.signInWithEmailAndPassword(email, password){ result ->
            view?.hideProgress()
            result.onSuccess {
                view?.showMessage("${it?.user?.displayName} logueado!")
                view?.navigateToMain()
            }
            result.onFailure {
                view?.showMessage("Login fallido!")
                //TODO: Controlar estos dos casos
                //SignInResult.INVALID_USER -> Toast.makeText(this, "Tienes que registrarte primero", Toast.LENGTH_LONG).show()
                //SignInResult.INVALID_CREDENTIALS -> Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length > 5
    }

    fun fromGoogleSignInResult(data: Intent?) {
        googleSignInRepository.getSignedAccountTokenFromIntent(data)?.let{ token ->
            authRepository.signInWithCredential(token) { result ->
                view?.hideProgress()
                result.onSuccess {
                    val user = authRepository.getCurrentUser()
                    view?.showMessage("${user?.displayName} logueado con google!")
                    view?.navigateToMain()
                }
                result.onFailure {
                    view?.showMessage("Error al logear con google!")
                }
            }
        }?: run{
            view?.hideProgress()
            view?.showMessage("Google sign in failed")
        }
    }

    fun onGoogleLoginClicked(activity: LoginActivity) {
        view?.showProgress()
        view?.loginWithGoogle(googleSignInRepository.getGoogleSignInIntent(activity, activity.getString(R.string.default_web_client_id)))
    }

    fun onDestroy() {
        this.view = null
    }



}