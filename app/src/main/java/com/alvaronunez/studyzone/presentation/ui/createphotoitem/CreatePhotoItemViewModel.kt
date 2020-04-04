package com.alvaronunez.studyzone.presentation.ui.createphotoitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaronunez.studyzone.presentation.ui.common.Event
import com.alvaronunez.studyzone.presentation.ui.common.ScopedViewModel
import kotlinx.coroutines.CoroutineDispatcher


class CreatePhotoItemViewModel(uiDispatcher: CoroutineDispatcher) : ScopedViewModel(uiDispatcher) {

    private val _takePhoto = MutableLiveData<Event<Unit>>()
    val takePhoto: LiveData<Event<Unit>> get() = _takePhoto

    private val _addItem = MutableLiveData<Event<Unit>>()
    val addItem: LiveData<Event<Unit>> get() = _addItem



    init {
        initScope()
    }

    fun onFabPhotoItemClicked() {
        _takePhoto.value = Event(Unit)
    }

    fun onAddItemClicked() {
        _addItem.value = Event(Unit)
    }


    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

}
