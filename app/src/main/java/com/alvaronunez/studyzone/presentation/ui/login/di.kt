package com.alvaronunez.studyzone.presentation.ui.login

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.usecases.SignInWithEmailAndPassword
import com.alvaronunez.studyzone.usecases.SignInWithGoogleCredential
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class LoginActivityModule {

    @Provides
    fun loginViewModelProvider(signInWithGoogleCredential: SignInWithGoogleCredential,
                               signInWithEmailAndPassword: SignInWithEmailAndPassword
    ) = LoginViewModel(signInWithGoogleCredential,signInWithEmailAndPassword)

    @Provides
    fun signInWithEmailAndPassword(authenticationRepository: AuthenticationRepository) = SignInWithEmailAndPassword(authenticationRepository)

    @Provides
    fun signinWithGoogleCredential(authenticationRepository: AuthenticationRepository) = SignInWithGoogleCredential(authenticationRepository)
}

@Subcomponent(modules = [(LoginActivityModule::class)])
interface LoginActivityComponent {
    val loginViewModel: LoginViewModel
}