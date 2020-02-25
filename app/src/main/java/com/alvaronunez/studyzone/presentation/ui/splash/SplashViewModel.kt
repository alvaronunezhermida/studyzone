package com.alvaronunez.studyzone.presentation.ui.splash

import android.os.Handler //TODO: Revisar si este import debe ir en el viewmodel o en el activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alvaronunez.studyzone.presentation.ui.common.*
import com.alvaronunez.studyzone.usecases.GetSignedUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class SplashViewModel(private val getSignedUser: GetSignedUser,
                      uiDispatcher: CoroutineDispatcher,
                      private val handler: Handler) : ScopedViewModel(uiDispatcher) {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() = _model

    sealed class UiModel {
        object NavigateToMain : UiModel()
        object NavigateToLogin : UiModel()
    }

    init {
        initScope()
        waitSplash()
    }

    private fun waitSplash() {
        handler.postDelayed({
            launch {
                getSignedUser.invoke()?.let {
                    _model.value = UiModel.NavigateToMain
                }?: run {
                    _model.value = UiModel.NavigateToLogin
                }
            }
        }, 1000)
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }

}