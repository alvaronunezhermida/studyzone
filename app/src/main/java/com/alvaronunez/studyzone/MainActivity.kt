package com.alvaronunez.studyzone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alvaronunez.studyzone.data.model.AuthRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val authRepository : AuthRepository by lazy { AuthRepository() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logout.setOnClickListener {
            authRepository.signOut()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_LONG).show()
            finish()
        }

    }
}
