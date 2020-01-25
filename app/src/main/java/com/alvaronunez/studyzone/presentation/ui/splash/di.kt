package com.alvaronunez.studyzone.presentation.ui.splash

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.usecases.GetSignedUser
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class SplashActivityModule {

    @Provides
    fun splashViewModel(getSignedUser: GetSignedUser) = SplashViewModel(getSignedUser)

    @Provides
    fun getSignedUser(authenticationRepository: AuthenticationRepository) = GetSignedUser(authenticationRepository)
}

@Subcomponent(modules = [(SplashActivityModule::class)])
interface  SplashActivityComponent {
    val splashViewModel: SplashViewModel
}