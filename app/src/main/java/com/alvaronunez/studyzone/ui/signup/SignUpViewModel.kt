package com.alvaronunez.studyzone.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.DatabaseRepository
import com.alvaronunez.studyzone.data.model.UserDTO

class SignUpViewModel : ViewModel() {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) _model.value = UiModel.NoState
            return _model
        }

    private val authRepository : AuthRepository by lazy { AuthRepository() }
    private val databaseRepository : DatabaseRepository by lazy { DatabaseRepository() }

    sealed class UiModel {
        object NoState : UiModel()
        object Loading : UiModel()
        class Message(val message: String) : UiModel()
        object NavigateToMain : UiModel()
    }

    fun onSignUpClicked(email: String, password: String, name: String, lastName: String) {
        _model.value = UiModel.Loading
        authRepository.signUpNewUser(email, password, "$name $lastName") { result ->
            result.onSuccess {
                it.user?.let { currentUser ->
                    databaseRepository.saveUserDB(
                        currentUser.uid,
                        UserDTO(name, lastName, email)
                    ) { result ->
                        result.onSuccess {
                            _model.value = UiModel.Message("${currentUser.displayName} registrado!")
                            _model.value = UiModel.NavigateToMain
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
                _model.value = UiModel.Message("Authentication failed.")
            }
        }
    }

    private fun saveUserFailed() {
        authRepository.removeCurrentUser()
        _model.value = UiModel.Message("Authentication failed.")
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

}

@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        SignUpViewModel() as T
}