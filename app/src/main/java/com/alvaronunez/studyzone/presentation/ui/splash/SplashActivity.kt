package com.alvaronunez.studyzone.presentation.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.ui.login.LoginActivity
import com.alvaronunez.studyzone.presentation.ui.main.MainActivity
import org.jetbrains.anko.startActivity
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by currentScope.viewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.model.observe(this, Observer(::updateUi))

    }

    private fun updateUi(model: SplashViewModel.UiModel) {
        when(model) {
            is SplashViewModel.UiModel.NavigateToMain -> {
                startActivity<MainActivity>()
                finish()
            }
            is SplashViewModel.UiModel.NavigateToLogin -> {
                startActivity<LoginActivity>()
                finish()
            }
        }

    }
}
