package com.alvaronunez.studyzone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.alvaronunez.studyzone.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        waitAndGoToMain()
    }

    private fun waitAndGoToMain() {
        Handler().postDelayed({
            mAuth.currentUser?.let {
                startActivity<MainActivity>()
            }?: run {
                startActivity<LoginActivity>()
            }
            finish()
        }, 2000)
    }
}
