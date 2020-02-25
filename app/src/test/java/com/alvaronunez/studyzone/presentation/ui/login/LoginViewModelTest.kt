package com.alvaronunez.studyzone.presentation.ui.login

import android.os.Handler
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.domain.User
import com.alvaronunez.studyzone.usecases.SignInWithEmailAndPassword
import com.alvaronunez.studyzone.usecases.SignInWithGoogleCredential
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var signInWithGoogleCredential: SignInWithGoogleCredential

    @Mock
    lateinit var signInWithEmailAndPassword: SignInWithEmailAndPassword

    @Mock
    lateinit var observer: Observer<LoginViewModel.UiModel>

    private lateinit var vm: LoginViewModel

    @Before
    fun setUp() {
        vm = LoginViewModel(signInWithGoogleCredential, signInWithEmailAndPassword, Dispatchers.Unconfined)
    }

    @Test
    fun `check onSignUpClicked navigate to signup`() {
        vm.onSignUpClicked()
        verify(observer).onChanged(LoginViewModel.UiModel.NavigateToSignUp)
    }

    @Test
    fun `check onLoginClicked navigates to main`() {
        vm.onLoginClicked(mock(), mock())
        verify(observer).onChanged(LoginViewModel.UiModel.NavigateToMain)
    }

    private val mockedUser = User(
        "0",
        "Name",
        "Last Name",
        "email@email.com",
        "Display Name"
    )

}