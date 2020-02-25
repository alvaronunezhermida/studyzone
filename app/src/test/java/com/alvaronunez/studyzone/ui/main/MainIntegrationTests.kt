package com.alvaronunez.studyzone.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.initMockedDi
import com.alvaronunez.studyzone.presentation.ui.main.MainViewModel
import com.alvaronunez.studyzone.usecases.GetItemsByUser
import com.alvaronunez.studyzone.usecases.GetSignedUser
import com.alvaronunez.studyzone.usecases.SignOutSignedUser
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
class MainIntegrationTests : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var openFabs: Observer<Boolean>

    private lateinit var vm: MainViewModel

    @Before
    fun setUp() {
        val vmModule = module {
            factory { MainViewModel(get(), get(), get(), get()) }
            factory { GetItemsByUser(get()) }
            factory { GetSignedUser(get()) }
            factory { SignOutSignedUser(get()) }
        }

        initMockedDi(vmModule)
        vm = get()
    }

    @Test
    fun `fabs value changes when is clicked`() {
        vm.openFabs.observeForever(openFabs)
        vm.onFabClicked()
        verify(openFabs).onChanged(true)
    }



}