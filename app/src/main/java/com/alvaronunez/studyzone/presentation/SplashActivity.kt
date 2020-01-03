package com.alvaronunez.studyzone.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.AuthRepository
import com.alvaronunez.studyzone.presentation.login.LoginActivity
import com.alvaronunez.studyzone.presentation.main.MainActivity
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private val authRepository : AuthRepository by lazy { AuthRepository() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        waitAndGoToMain()
    }

    private fun waitAndGoToMain() {
        Handler().postDelayed({
            if(authRepository.thereIsUserSigned()){
                startActivity<MainActivity>()
            }else{
                startActivity<LoginActivity>()
            }
            finish()
        }, 1000)
    }
}
