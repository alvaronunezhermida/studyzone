package com.alvaronunez.studyzone.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.DatabaseRepository
import com.alvaronunez.studyzone.data.model.ItemDTO
import com.alvaronunez.studyzone.ui.common.Scope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(), Scope by Scope.Impl() {

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

    init {
        initScope()
    }

    private fun refresh() {
        _model.value = UiModel.Loading
        launch {
            databaseRepository.getItemsByUser(authRepository.getCurrentUser()?.uid)?.let { items ->
                _model.value = UiModel.Content(items)
            }?: run {
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

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MainViewModel() as T
}