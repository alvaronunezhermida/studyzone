package com.alvaronunez.studyzone.presentation.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.ui.signup.SignUpViewModel.FormModel
import com.alvaronunez.studyzone.presentation.ui.signup.SignUpViewModel.UiModel
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by currentScope.viewModel(this)
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            is UiModel.Message -> Toast.makeText(this.context, model.message, Toast.LENGTH_LONG).show()
            is UiModel.NavigateToMain -> {
                navController.navigate(R.id.action_signUpFragment_to_mainFragment)
            }
            is UiModel.Content -> viewModel.onSignUpClicked(model.user, etLastName.text.toString())
        }
    }

}