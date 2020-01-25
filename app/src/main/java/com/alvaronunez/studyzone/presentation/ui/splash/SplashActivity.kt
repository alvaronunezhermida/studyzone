package com.alvaronunez.studyzone.presentation.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.ui.common.app
import com.alvaronunez.studyzone.presentation.ui.common.getViewModel
import com.alvaronunez.studyzone.presentation.ui.login.LoginActivity
import com.alvaronunez.studyzone.presentation.ui.main.MainActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {


    private lateinit var component: SplashActivityComponent
    private val viewModel: SplashViewModel by lazy { getViewModel { component.splashViewModel } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        component = app.component.plus(SplashActivityModule())

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
