package com.alvaronunez.studyzone.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.DatabaseRepository
import com.alvaronunez.studyzone.data.model.UserDTO
import com.alvaronunez.studyzone.ui.common.isValidConfirmedPassword
import com.alvaronunez.studyzone.ui.common.isValidEmail
import com.alvaronunez.studyzone.ui.common.isValidName
import com.alvaronunez.studyzone.ui.common.isValidPassword

class SignUpViewModel : ViewModel() {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() = _model

    private val _formModel = MutableLiveData<FormModel>()
    val formModel: LiveData<FormModel>
        get() = _formModel

    private val authRepository : AuthRepository by lazy { AuthRepository() }
    private val databaseRepository : DatabaseRepository by lazy { DatabaseRepository() }

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
        authRepository.signUpNewUser(userUIForm.email, userUIForm.password, "${userUIForm.name} $lastName") { result ->
            result.onSuccess {
                it.user?.let { currentUser ->
                    databaseRepository.saveUserDB(
                        currentUser.uid,
                        UserDTO(userUIForm.name, lastName, userUIForm.email)
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

}

@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        SignUpViewModel() as T
}