package com.alvaronunez.studyzone.presentation.ui.splash

import android.os.Handler
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.domain.User
import com.alvaronunez.studyzone.presentation.ui.common.Event
import com.alvaronunez.studyzone.usecases.GetSignedUser
import com.nhaarman.mockitokotlin2.any
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
class SplashViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getSignedUser: GetSignedUser

    @Mock
    lateinit var handler: Handler

    @Mock
    lateinit var observer: Observer<SplashViewModel.UiModel>

    private lateinit var vm: SplashViewModel

    @Before
    fun setUp() {
        vm = SplashViewModel(getSignedUser, Dispatchers.Unconfined, handler)
    }

    @Test
    fun `check navigate to main when have user signed`() {
        runBlocking {
            whenever(getSignedUser.invoke()).thenReturn(mockedUser)
        }
        verify(observer).onChanged(SplashViewModel.UiModel.NavigateToMain)
    }

    private val mockedUser = User(
        "0",
        "Name",
        "Last Name",
        "email@email.com",
        "Display Name"
    )

}