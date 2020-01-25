package com.alvaronunez.studyzone.presentation.ui.createitem

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.usecases.AddItem
import com.alvaronunez.studyzone.usecases.GetCategoriesByUser
import com.alvaronunez.studyzone.usecases.GetSignedUser
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class CreateItemActivityModule {

    @Provides
    fun createItemViewModelProvider(additem: AddItem, getCategoriesByUser: GetCategoriesByUser, getSignedUser: GetSignedUser) = CreateItemViewModel(getCategoriesByUser, getSignedUser, additem)

    @Provides
    fun addItem(repository: Repository) = AddItem(repository)

    @Provides
    fun getCategoriesByUser(repository: Repository) = GetCategoriesByUser(repository)

    @Provides
    fun getSignedUser(authenticationRepository: AuthenticationRepository) = GetSignedUser(authenticationRepository)
}

@Subcomponent(modules = [(CreateItemActivityModule::class)])
interface CreateItemActivityComponent {
    val createItemViewModel: CreateItemViewModel
}