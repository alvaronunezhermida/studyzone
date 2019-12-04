package com.alvaronunez.studyzone.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaronunez.studyzone.MainActivity
import com.alvaronunez.studyzone.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.startActivity


class SignUpActivity : AppCompatActivity(), SignUpPresenter.View {

    private val presenter by lazy { SignUpPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        presenter.onCreate(this)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    private fun isFormValid(): Boolean {
        if (!presenter.isValidName(userName.text.toString())) {
            userName.error = "Campo vacío"
            return false
        }
        if (!presenter.isValidEmail(userEmail.text.toString())) {
            userEmail.error = "Formato inválido"
            return false
        }
        if(!presenter.isValidPassword(etPassword.text.toString())){
            etPassword.error = "Mínimo 6 caracteres"
            return false
        }
        if (!presenter.isValidConfirmedPassword(etPassword.text.toString(), etConfirmPassword.text.toString())){
            etConfirmPassword.error = "No coincide con la contraseña"
            return false
        }
        return true
    }

    override fun setListeners() {
        sign_up.setOnClickListener {
            if (isFormValid()){
                presenter.onSignUpClicked(
                    userEmail.text.toString(),
                    etPassword.text.toString(),
                    userName.text.toString(),
                    lastName.text.toString()
                )
            }
        }

        val focusListener = View.OnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) isFormValid()
        }

        userName.onFocusChangeListener = focusListener
        userEmail.onFocusChangeListener = focusListener
        etPassword.onFocusChangeListener = focusListener
        etConfirmPassword.onFocusChangeListener = focusListener
    }

    override fun showProgress() {
        loading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        loading.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun navigateToMain() {
        startActivity<MainActivity>()
        finish()
    }
}
