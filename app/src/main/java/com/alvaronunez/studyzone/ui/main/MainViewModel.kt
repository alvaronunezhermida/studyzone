package com.alvaronunez.studyzone.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.data.model.DatabaseRepository
import com.alvaronunez.studyzone.data.model.ItemDTO
import com.alvaronunez.studyzone.ui.common.Event
import com.alvaronunez.studyzone.ui.common.Scope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(), Scope by Scope.Impl() {

    private val _items = MutableLiveData<List<ItemDTO>>()
    val items: LiveData<List<ItemDTO>> get() = _items

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _finish = MutableLiveData<Event<Unit>>()
    val finish: LiveData<Event<Unit>> get() = _finish

    private val _navigateToCreateItem = MutableLiveData<Event<Unit>>()
    val navigateToCreateItem: LiveData<Event<Unit>> get() = _navigateToCreateItem

    private val _areFabsOpened = MutableLiveData<Boolean>()
    val areFabsOpened: LiveData<Boolean>
        get() {
            if(_areFabsOpened.value == null) _areFabsOpened.value = false
            return _areFabsOpened
        }


    private val databaseRepository : DatabaseRepository by lazy { DatabaseRepository() }
    private val authRepository : AuthRepository by lazy { AuthRepository() }

    sealed class FabsModel {
        object Opened : FabsModel()
        object Closed : FabsModel()
    }

    init {
        initScope()
        refresh()
    }

    private fun refresh() {
        launch {
            _loading.value = true
            _items.value = databaseRepository.getItemsByUser(authRepository.getCurrentUser()?.uid)
            _loading.value = false
        }
    }

    fun onItemClicked(item: ItemDTO) {
        _message.value = "${item.title} clicked!"
    }

    fun onSignOutClicked() {
        authRepository.signOut()
        _message.value = "Sesión cerrada"
        _finish.value = Event(Unit)
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

    fun onFabClicked() {
        val areOpened: Boolean = _areFabsOpened.value?:false
        _areFabsOpened.value = areOpened.not()
    }

    fun onFabItemClicked() {
        _navigateToCreateItem.value = Event(Unit)
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MainViewModel() as T
}