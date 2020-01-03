package com.alvaronunez.studyzone.presentation.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alvaronunez.studyzone.presentation.main.MainActivity
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.signup.SignUpViewModel.UiModel
import com.alvaronunez.studyzone.presentation.signup.SignUpViewModel.FormModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.startActivity


class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModel: SignUpViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setListeners()
        viewModelSetUp()
    }

    private fun setListeners() {
        sign_up.setOnClickListener {
            val userEmail = etUserEmail.text.toString()
            val password = etPassword.text.toString()
            val userName = etUserName.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            viewModel.isFormValid(userEmail,userName,password,confirmPassword)
        }
    }

    private fun viewModelSetUp() {
        viewModel = ViewModelProviders.of(
            this,
            SignUpViewModelFactory())[SignUpViewModel::class.java]

        viewModel.model.observe(this, Observer(::updateUi))
        viewModel.formModel.observe(this, Observer(::updateFormError))
    }

    private fun updateFormError(formModel: FormModel) {
        when(formModel){
            is FormModel.Email -> userEmail.error = formModel.error
            is FormModel.Name -> etUserName.error = formModel.error
            is FormModel.Password -> etPassword.error = formModel.error
            is FormModel.ConfirmPassword -> etConfirmPassword.error = formModel.error
        }
    }

    private fun updateUi(model: UiModel) {
        loading.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE
        when (model) {
            is UiModel.Message -> Toast.makeText(this, model.message, Toast.LENGTH_LONG).show()
            is UiModel.NavigateToMain -> {
                startActivity<MainActivity>()
                finish()
            }
            is UiModel.Content -> viewModel.onSignUpClicked(model.user, etLastName.text.toString())
        }
    }
}
