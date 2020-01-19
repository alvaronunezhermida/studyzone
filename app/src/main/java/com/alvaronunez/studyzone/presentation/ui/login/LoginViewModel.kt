package com.alvaronunez.studyzone.presentation.ui.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaronunez.studyzone.presentation.ui.common.ScopedViewModel
import com.alvaronunez.studyzone.presentation.ui.common.isValidEmail
import com.alvaronunez.studyzone.presentation.ui.common.isValidPassword
import com.alvaronunez.studyzone.usecases.SignInWithEmailAndPassword
import com.alvaronunez.studyzone.usecases.SignInWithGoogleCredential
import kotlinx.coroutines.launch

class LoginViewModel(private val signInWithGoogleCredential: SignInWithGoogleCredential,
                     private val signInWithEmailAndPassword: SignInWithEmailAndPassword) : ScopedViewModel() {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() = _model

    private val _formModel = MutableLiveData<FormModel>()
    val formModel: LiveData<FormModel>
        get() = _formModel

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

    init {
        initScope()
    }


    fun onSignUpClicked() {
        _model.value = UiModel.NavigateToSignUp
    }

    fun onLoginClicked(email: String, password: String) {
        _model.value = UiModel.Loading
        launch {
            signInWithEmailAndPassword.invoke(email, password)?.let {user ->
                _model.value = UiModel.Message("${user.displayName} logueado!")
                _model.value = UiModel.NavigateToMain
            }?: run {
                _model.value = UiModel.Message("Login fallido!")
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

    fun fromGoogleSignInResult(token: String?) {
        token?.let{
            _model.value = UiModel.Loading
            launch {
                signInWithGoogleCredential.invoke(it)?.let {user ->
                    _model.value = UiModel.Message("${user.displayName} logueado con google!")
                    _model.value = UiModel.NavigateToMain
                }?: run {
                    _model.value = UiModel.Message("Error al logear con google!")
                }
            }
        }?: run{
            _model.value = UiModel.Message("Google sign in failed")
        }
    }

    fun onGoogleLoginClicked(intent: Intent) {
        _model.value = UiModel.Loading
        _model.value = UiModel.LoginWithGoogle(intent)
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

}