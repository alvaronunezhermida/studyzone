package com.alvaronunez.studyzone.presentation.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.presentation.ui.common.Event
import com.alvaronunez.studyzone.presentation.ui.initMockedDi
import com.alvaronunez.studyzone.usecases.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginIntegrationTests : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<LoginViewModel.UiModel>

    private lateinit var vm: LoginViewModel

    @Before
    fun setUp() {
        val vmModule = module {
            factory { LoginViewModel(get(), get(), get()) }
            factory { SignInWithGoogleCredential(get()) }
            factory { SignInWithEmailAndPassword(get()) }
        }

        initMockedDi(vmModule)
        vm = get()
    }

    @Test
    fun `onSignupClicked navigates to signup`() {
        vm.model.observeForever(observer)
        vm.onSignUpClicked()
        verify(observer).onChanged(LoginViewModel.UiModel.NavigateToSignUp)
    }

    @Test
    fun `onLoginClicked navigates to main`() {
        vm.model.observeForever(observer)
        vm.onLoginClicked("", "")
        verify(observer).onChanged(LoginViewModel.UiModel.NavigateToMain)
    }

    @Test
    fun `fromGoogleSignInResult navigates to main`() {
        vm.model.observeForever(observer)
        vm.fromGoogleSignInResult("")
        verify(observer).onChanged(LoginViewModel.UiModel.NavigateToMain)
    }



}