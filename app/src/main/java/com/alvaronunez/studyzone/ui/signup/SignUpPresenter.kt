package com.alvaronunez.studyzone.ui.signup

import android.util.Patterns
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.DatabaseRepository
import com.alvaronunez.studyzone.data.model.UserDTO

class SignUpPresenter {

    interface View {
        fun setListeners()
        fun showProgress()
        fun hideProgress()
        fun showMessage(message: String)
        fun navigateToMain()
    }

    private val authRepository : AuthRepository by lazy { AuthRepository() }
    private val databaseRepository : DatabaseRepository by lazy { DatabaseRepository() }
    private var view: View? = null

    fun onCreate(view: View) {
        this.view = view
        view.setListeners()
    }

    fun onSignUpClicked(email: String, password: String, name: String, lastName: String) {
        view?.showProgress()
        authRepository.signUpNewUser(email, password, "$name $lastName") { result ->
            result.onSuccess {
                it.user?.let { currentUser ->
                    databaseRepository.saveUserDB(
                        currentUser.uid,
                        UserDTO(name, lastName, email)
                    ) { result ->
                        result.onSuccess {
                            view?.hideProgress()
                            view?.showMessage("${currentUser.displayName} registrado!")
                            view?.navigateToMain()
                        }
                        result.onFailure {
                            saveUserFailed()
                        }
                    }
                }?: run {
                    saveUserFailed()
                }
            }
            result.onFailure {
                //TODO: Controlar email inválido
                //userEmail.error = "Formato inválido"
                view?.hideProgress()
                view?.showMessage("Authentication failed.")
            }
        }
    }

    private fun saveUserFailed() {
        authRepository.removeCurrentUser()
        view?.showMessage("Authentication failed.")
    }

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

    fun onDestroy() {
        this.view = null
    }



}