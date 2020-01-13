package com.alvaronunez.studyzone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.alvaronunez.studyzone.data.model.AuthRepository
import com.alvaronunez.studyzone.ui.login.LoginActivity
import com.alvaronunez.studyzone.ui.main.MainActivity
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
