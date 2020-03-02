package com.alvaronunez.studyzone.presentation.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.alvaronunez.studyzone.R
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class SplashFragment : Fragment(){

    private val viewModel: SplashViewModel by currentScope.viewModel(this)
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        viewModel.model.observe(this, Observer(::updateUi))

    }

    private fun updateUi(model: SplashViewModel.UiModel) {
        when(model) {
            is SplashViewModel.UiModel.NavigateToMain -> {
                navController.navigate(R.id.action_splashFragment_to_mainFragment)
            }
            is SplashViewModel.UiModel.NavigateToLogin -> {
                navController.navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }

    }

}