package com.alvaronunez.studyzone.presentation.ui.main

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.usecases.GetItemsByUser
import com.alvaronunez.studyzone.usecases.GetSignedUser
import com.alvaronunez.studyzone.usecases.SignOutSignedUser
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class MainActivityModule {

    @Provides
    fun mainViewModelProvider(getItemsByUser: GetItemsByUser, getSignedUser: GetSignedUser, signOutSignedUser: SignOutSignedUser) = MainViewModel(getItemsByUser, getSignedUser, signOutSignedUser)

    @Provides
    fun getItemsByUser(repository: Repository) = GetItemsByUser(repository)

    @Provides
    fun getSignedUser(authenticationRepository: AuthenticationRepository) = GetSignedUser(authenticationRepository)

    @Provides
    fun signOutSignedUser(authenticationRepository: AuthenticationRepository) = SignOutSignedUser(authenticationRepository)
}

@Subcomponent(modules = [(MainActivityModule::class)])
interface MainActivityComponent {
    val mainViewModel: MainViewModel
}