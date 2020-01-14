package com.alvaronunez.studyzone.presentation.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaronunez.studyzone.data.AuthRepository
import com.alvaronunez.studyzone.domain.User
import com.alvaronunez.studyzone.presentation.ui.common.*
import com.alvaronunez.studyzone.usecases.SaveUser
import kotlinx.coroutines.launch

class SignUpViewModel(private val saveUser: SaveUser) : ScopedViewModel() {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() = _model

    private val _formModel = MutableLiveData<FormModel>()
    val formModel: LiveData<FormModel>
        get() = _formModel

    private val authRepository : AuthRepository by lazy { AuthRepository() }

    sealed class UiModel {
        object Loading : UiModel()
        class Message(val message: String) : UiModel()
        object NavigateToMain : UiModel()
        class Content(val user: UserUIForm): UiModel()
    }

    sealed class FormModel {
        class Email(val error: String) : FormModel()
        class Name(val error: String) : FormModel()
        class Password(val error: String) : FormModel()
        class ConfirmPassword(val error: String) : FormModel()
    }

    data class UserUIForm(val email: String, val name: String, val password: String, val confirmPassword: String)

    init {
        initScope()
    }

    fun isFormValid(email: String, name: String, password: String, confirmPassword: String): Boolean {
        return when {
            !isValidName(name) -> {
                _formModel.value = FormModel.Name("Campo vacío")
                false
            }
            !isValidEmail(email) -> {
                _formModel.value = FormModel.Email("Formato inválido")
                false
            }
            !isValidPassword(password) -> {
                _formModel.value = FormModel.Password("Mínimo 6 caracteres")
                false
            }
            !isValidConfirmedPassword(password, confirmPassword) -> {
                _formModel.value = FormModel.ConfirmPassword("No coincide con la contraseña")
                false
            }
            else -> {
                _model.value = UiModel.Content(UserUIForm(email, name, password, confirmPassword))
                true
            }
        }
    }

    fun onSignUpClicked(userUIForm: UserUIForm, lastName: String) {
        _model.value = UiModel.Loading
        launch {
            authRepository.signUpNewUser(userUIForm.email, userUIForm.password, "${userUIForm.name} $lastName")?.let {result ->
                result.user?.let { currentUser ->
                    if(saveUser.invoke(User(
                            id = currentUser.uid,
                            name = userUIForm.name,
                            lastName = lastName,
                            email = userUIForm.email
                        ))){
                        _model.value = UiModel.Message("${currentUser.displayName} registrado!")
                        _model.value = UiModel.NavigateToMain
                    }else{
                        saveUserFailed()
                    }
                }?: run {
                    saveUserFailed()
                }
            }?: run {
                _model.value = UiModel.Message("Authentication failed.")
            }
        }
    }

    private fun saveUserFailed() {
        authRepository.removeCurrentUser()
        _model.value = UiModel.Message("Authentication failed.")
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

}