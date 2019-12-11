package com.alvaronunez.studyzone.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.DatabaseRepository
import com.alvaronunez.studyzone.data.model.ItemDTO

class MainViewModel : ViewModel() {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }


    private val databaseRepository : DatabaseRepository by lazy { DatabaseRepository() }
    private val authRepository : AuthRepository by lazy { AuthRepository() }

    sealed class UiModel {
        object Loading : UiModel()
        class Content(val items: List<ItemDTO>) : UiModel()
        class Message(val message: String) : UiModel()
        object Finish : UiModel()
    }

    private fun refresh() {
        _model.value = UiModel.Loading
        databaseRepository.getItemsByUser(authRepository.getCurrentUser()?.uid){ result ->
            result.onSuccess { itemList ->
                _model.value = UiModel.Content(itemList)
            }
            result.onFailure {
                _model.value = UiModel.Message("Error al leer datos!")
            }
        }
    }

    fun onItemClicked(item: ItemDTO) {
        _model.value = UiModel.Message("${item.title} clicked!")
    }

    fun onSignOutClicked() {
        authRepository.signOut()
        _model.value = UiModel.Message("Sesión cerrada")
        _model.value = UiModel.Finish
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MainViewModel() as T
}