package com.alvaronunez.studyzone.presentation.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.presentation.ui.common.Event
import com.alvaronunez.studyzone.usecases.GetItemsByUser
import com.alvaronunez.studyzone.usecases.GetSignedUser
import com.alvaronunez.studyzone.usecases.SignOutSignedUser
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getItemsByUser: GetItemsByUser

    @Mock
    lateinit var getSignedUser: GetSignedUser

    @Mock
    lateinit var signOutSignedUser: SignOutSignedUser

    private lateinit var vm: MainViewModel

    @Before
    fun setUp() {
        vm = MainViewModel(getItemsByUser, getSignedUser, signOutSignedUser, Dispatchers.Unconfined)
    }

    @Test
    fun `check onFabClicked sets openFabs livedata opposite`() {
        val openFabsObserver: Observer<Boolean> = mock()
        vm.openFabs.observeForever(openFabsObserver)
        vm.onFabClicked()

        verify(openFabsObserver).onChanged(true)
    }

    @Test
    fun `after click signout finish is called`(){
        val finishObserver: Observer<Event<Unit>> = mock()
        vm.finish.observeForever(finishObserver)
        vm.onSignOutClicked()

        verify(finishObserver).onChanged(any())
    }

}