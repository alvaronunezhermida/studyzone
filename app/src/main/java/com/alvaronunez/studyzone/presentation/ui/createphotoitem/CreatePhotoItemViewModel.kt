package com.alvaronunez.studyzone.presentation.ui.createphotoitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaronunez.studyzone.presentation.ui.common.Event
import com.alvaronunez.studyzone.presentation.ui.common.ScopedViewModel


class CreatePhotoItemViewModel : ScopedViewModel() {

    private val _takePhoto = MutableLiveData<Event<Unit>>()
    val takePhoto: LiveData<Event<Unit>> get() = _takePhoto



    init {
        initScope()
    }

    fun onFabPhotoItemClicked() {
        _takePhoto.value = Event(Unit)
    }


    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

}
