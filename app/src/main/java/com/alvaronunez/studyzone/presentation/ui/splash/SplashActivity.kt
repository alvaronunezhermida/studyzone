package com.alvaronunez.studyzone.presentation.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.presentation.data.FirebaseAuthDataSource
import com.alvaronunez.studyzone.presentation.ui.common.getViewModel
import com.alvaronunez.studyzone.presentation.ui.login.LoginActivity
import com.alvaronunez.studyzone.presentation.ui.main.MainActivity
import com.alvaronunez.studyzone.usecases.GetSignedUser
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel = getViewModel { SplashViewModel(GetSignedUser(AuthenticationRepository(FirebaseAuthDataSource()))) }
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
