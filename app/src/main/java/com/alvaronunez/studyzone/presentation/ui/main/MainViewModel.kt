package com.alvaronunez.studyzone.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.presentation.ui.common.Event
import com.alvaronunez.studyzone.presentation.ui.common.ScopedViewModel
import com.alvaronunez.studyzone.usecases.GetItemsByUser
import com.alvaronunez.studyzone.usecases.GetSignedUser
import com.alvaronunez.studyzone.usecases.SignOutSignedUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainViewModel(private val getItemsByUser: GetItemsByUser,
                    private val getSignedUser: GetSignedUser,
                    private val signOutSignedUser: SignOutSignedUser,
                    uiDispatcher: CoroutineDispatcher) : ScopedViewModel(uiDispatcher) {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _finish = MutableLiveData<Event<Unit>>()
    val finish: LiveData<Event<Unit>> get() = _finish

    private val _navigateToCreateItem = MutableLiveData<Event<Unit>>()
    val navigateToCreateItem: LiveData<Event<Unit>> get() = _navigateToCreateItem

    private val _navigateToCamera = MutableLiveData<Event<Unit>>()
    val navigateToCamera: LiveData<Event<Unit>> get() = _navigateToCamera

    private val _openFabs = MutableLiveData<Boolean>()
    val openFabs: LiveData<Boolean>
        get() {
            if(_openFabs.value == null) _openFabs.value = false
            return _openFabs
        }

    init {
        initScope()
        refresh()
    }

    private fun refresh() {
        launch {
            _loading.value = true
            getSignedUser.invoke()?.let {user ->
                _items.value = getItemsByUser.invoke(user.id)
            }
            _loading.value = false
        }
    }

    fun onItemClicked(item: Item) {
        _message.value = "${item.title} clicked!"
    }

    fun onSignOutClicked() {
        launch {
            signOutSignedUser.invoke()
            _message.value = "Sesión cerrada"
            _finish.value = Event(Unit)
        }
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

    fun onFabClicked() {
        val openFabs: Boolean = _openFabs.value?:false
        _openFabs.value = openFabs.not()
    }

    fun onFabItemClicked() {
        _navigateToCreateItem.value = Event(Unit)
    }

    fun onFabPhotoItemClicked() {
        _navigateToCamera.value = Event(Unit)
    }
}