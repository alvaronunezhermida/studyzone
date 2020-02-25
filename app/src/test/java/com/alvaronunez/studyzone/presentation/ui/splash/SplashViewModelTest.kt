package com.alvaronunez.studyzone.presentation.ui.splash

import android.os.Handler
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.alvaronunez.studyzone.domain.User
import com.alvaronunez.studyzone.usecases.GetSignedUser
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
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
        whenever(handler.postDelayed(any(Runnable::class.java), anyLong())).thenAnswer {
            it.getArgument(0,Runnable::class.java).run()
            null
        }
        runBlocking {
            whenever(getSignedUser.invoke()).thenReturn(mockedUser)
            vm = SplashViewModel(getSignedUser, Dispatchers.Unconfined, handler)
        }
    }

    @Test
    fun `check navigate to main when have user signed`() {
        runBlocking {
            vm.model.observeForever(observer)
            verify(observer).onChanged(SplashViewModel.UiModel.NavigateToMain)
        }
    }

    private val mockedUser = User(
        "0",
        "Name",
        "Last Name",
        "email@email.com",
        "Display Name"
    )

}