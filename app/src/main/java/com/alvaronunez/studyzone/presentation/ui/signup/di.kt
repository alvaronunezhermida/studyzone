package com.alvaronunez.studyzone.presentation.ui.signup

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.usecases.RemoveSignedUser
import com.alvaronunez.studyzone.usecases.SaveUser
import com.alvaronunez.studyzone.usecases.SignUpNewUser
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class SignUpActivityModule {

    @Provides
    fun signUpViewModelProvider(saveUser: SaveUser, signUpNewUser: SignUpNewUser, removeSignedUser: RemoveSignedUser) = SignUpViewModel(saveUser, signUpNewUser, removeSignedUser)

    @Provides
    fun saveUser(repository: Repository) = SaveUser(repository)

    @Provides
    fun signUpNewUser(authenticationRepository: AuthenticationRepository) = SignUpNewUser(authenticationRepository)

    @Provides
    fun removeSignedUser(authenticationRepository: AuthenticationRepository) = RemoveSignedUser(authenticationRepository)
}

@Subcomponent(modules = [(SignUpActivityModule::class)])
interface SignUpActivityComponent {
    val signUpViewModel: SignUpViewModel
}