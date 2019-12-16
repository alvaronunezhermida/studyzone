package com.alvaronunez.studyzone.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alvaronunez.studyzone.ui.main.MainActivity
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.ui.signup.SignUpViewModel.UiModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.startActivity


class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setListeners()
        viewModel = ViewModelProviders.of(
            this,
            SignUpViewModelFactory())[SignUpViewModel::class.java]

        viewModel.model.observe(this, Observer(::updateUi))
    }

    private fun setListeners() {
        sign_up.setOnClickListener {
            if (isFormValid()){
                viewModel.onSignUpClicked(
                    userEmail.text.toString(),
                    etPassword.text.toString(),
                    userName.text.toString(),
                    lastName.text.toString()
                )
            }
        }

        val focusListener = View.OnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) isFormValid()
        }

        userName.onFocusChangeListener = focusListener
        userEmail.onFocusChangeListener = focusListener
        etPassword.onFocusChangeListener = focusListener
        etConfirmPassword.onFocusChangeListener = focusListener
    }

    private fun isFormValid(): Boolean {
        if (!viewModel.isValidName(userName.text.toString())) {
            userName.error = "Campo vacío"
            return false
        }
        if (!viewModel.isValidEmail(userEmail.text.toString())) {
            userEmail.error = "Formato inválido"
            return false
        }
        if(!viewModel.isValidPassword(etPassword.text.toString())){
            etPassword.error = "Mínimo 6 caracteres"
            return false
        }
        if (!viewModel.isValidConfirmedPassword(etPassword.text.toString(), etConfirmPassword.text.toString())){
            etConfirmPassword.error = "No coincide con la contraseña"
            return false
        }
        return true
    }

    private fun updateUi(model: UiModel) {
        loading.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is UiModel.Message -> Toast.makeText(this, model.message, Toast.LENGTH_LONG).show()
            is UiModel.NavigateToMain -> {
                startActivity<MainActivity>()
                finish()
            }

        }
    }
}
