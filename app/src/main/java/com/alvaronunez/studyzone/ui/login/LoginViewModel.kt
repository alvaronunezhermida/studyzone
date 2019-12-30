package com.alvaronunez.studyzone.ui.login

import android.content.Intent
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.GoogleSignInRepository
import com.alvaronunez.studyzone.ui.common.isValidEmail
import com.alvaronunez.studyzone.ui.common.isValidName
import com.alvaronunez.studyzone.ui.common.isValidPassword

class LoginViewModel : ViewModel() {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() = _model

    private val _formModel = MutableLiveData<FormModel>()
    val formModel: LiveData<FormModel>
        get() = _formModel

    private val authRepository : AuthRepository by lazy { AuthRepository() }
    private val googleSignInRepository : GoogleSignInRepository by lazy { GoogleSignInRepository() }

    sealed class UiModel {
        object Loading : UiModel()
        class Message(val message: String) : UiModel()
        object NavigateToMain : UiModel()
        object NavigateToSignUp : UiModel()
        class LoginWithGoogle(val intent: Intent) : UiModel()
        class Content(val email: String, val password: String) : UiModel()
    }

    sealed class FormModel {
        class Email(val error: String) : FormModel()
        class Password(val error: String) : FormModel()
    }


    fun onSignUpClicked() {
        _model.value = UiModel.NavigateToSignUp
    }

    fun onLoginClicked(email: String, password: String) {
        _model.value = UiModel.Loading
        authRepository.signInWithEmailAndPassword(email, password){ result ->
            result.onSuccess {
                _model.value = UiModel.Message("${it?.user?.displayName} logueado!")
                _model.value = UiModel.NavigateToMain
            }
            result.onFailure {
                _model.value = UiModel.Message("Login fallido!")
                //TODO: Controlar estos dos casos
                //SignInResult.INVALID_USER -> Toast.makeText(this, "Tienes que registrarte primero", Toast.LENGTH_LONG).show()
                //SignInResult.INVALID_CREDENTIALS -> Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun isFormValid(email: String, password: String): Boolean {
        return when {
            !isValidEmail(email) -> {
                _formModel.value = FormModel.Email("Formato inválido")
                false
            }
            !isValidPassword(password) -> {
                _formModel.value = FormModel.Password("Mínimo 6 caracteres")
                false
            }
            else ->{
                _model.value = UiModel.Content(email, password)
                true
            }
        }
    }

    fun fromGoogleSignInResult(data: Intent?) {
        googleSignInRepository.getSignedAccountTokenFromIntent(data)?.let{ token ->
            authRepository.signInWithCredential(token) { result ->
                _model.value = UiModel.Loading
                result.onSuccess {
                    val user = authRepository.getCurrentUser()
                    _model.value = UiModel.Message("${user?.displayName} logueado con google!")
                    _model.value = UiModel.NavigateToMain
                }
                result.onFailure {
                    _model.value = UiModel.Message("Error al logear con google!")
                }
            }
        }?: run{
            _model.value = UiModel.Message("Google sign in failed")
        }
    }

    fun onGoogleLoginClicked(activity: LoginActivity) {
        _model.value = UiModel.Loading
        _model.value = UiModel.LoginWithGoogle(googleSignInRepository.getGoogleSignInIntent(activity, activity.getString(R.string.default_web_client_id)))
    }

}

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        LoginViewModel() as T
}