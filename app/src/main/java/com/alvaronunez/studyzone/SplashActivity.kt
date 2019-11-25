package com.alvaronunez.studyzone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        waitAndGoToMain()
    }

    private fun waitAndGoToMain() {
        Handler().postDelayed({
            startActivity<MainActivity>()
            finish()
        }, 2000)
    }
}
