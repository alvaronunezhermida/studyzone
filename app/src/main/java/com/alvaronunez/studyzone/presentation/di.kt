package com.alvaronunez.studyzone.presentation

import android.app.Application
import android.os.Handler
import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.data.source.AuthenticationDataSource
import com.alvaronunez.studyzone.data.source.RemoteDataSource
import com.alvaronunez.studyzone.presentation.data.FirebaseAuthDataSource
import com.alvaronunez.studyzone.presentation.data.FirebaseDataSource
import com.alvaronunez.studyzone.presentation.ui.createitem.CreateItemFragment
import com.alvaronunez.studyzone.presentation.ui.createitem.CreateItemViewModel
import com.alvaronunez.studyzone.presentation.ui.createphotoitem.CreatePhotoItemFragment
import com.alvaronunez.studyzone.presentation.ui.createphotoitem.CreatePhotoItemViewModel
import com.alvaronunez.studyzone.presentation.ui.login.LoginFragment
import com.alvaronunez.studyzone.presentation.ui.login.LoginViewModel
import com.alvaronunez.studyzone.presentation.ui.main.MainFragment
import com.alvaronunez.studyzone.presentation.ui.main.MainViewModel
import com.alvaronunez.studyzone.presentation.ui.signup.SignUpFragment
import com.alvaronunez.studyzone.presentation.ui.signup.SignUpViewModel
import com.alvaronunez.studyzone.presentation.ui.splash.SplashFragment
import com.alvaronunez.studyzone.presentation.ui.splash.SplashViewModel
import com.alvaronunez.studyzone.usecases.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, scopesModule))
    }
}

private val appModule = module {
    factory<RemoteDataSource>{ FirebaseDataSource()}
    factory<AuthenticationDataSource>{ FirebaseAuthDataSource() }
    single<CoroutineDispatcher> { Dispatchers.Main }
    single { Handler() }
}

val dataModule = module {
    factory { Repository(get()) }
    factory { AuthenticationRepository(get()) }
}

private val scopesModule = module {
    scope(named<CreateItemFragment>()) {
        viewModel { CreateItemViewModel(get(), get(), get(), get()) }
        scoped { GetCategoriesByUser(get())}
        scoped { GetSignedUser(get()) }
        scoped { AddItem(get()) }
    }

    scope(named<LoginFragment>()) {
        viewModel { LoginViewModel(get(), get(), get()) }
        scoped { SignInWithEmailAndPassword(get())}
        scoped { SignInWithGoogleCredential(get()) }
    }

    scope(named<MainFragment>()) {
        viewModel { MainViewModel(get(), get(), get(), get()) }
        scoped { GetItemsByUser(get())}
        scoped { GetSignedUser(get()) }
        scoped { SignOutSignedUser(get()) }
    }

    scope(named<SignUpFragment>()) {
        viewModel { SignUpViewModel(get(), get(), get(), get()) }
        scoped { SaveUser(get())}
        scoped { SignUpNewUser(get()) }
        scoped { RemoveSignedUser(get()) }
    }

    scope(named<SplashFragment>()) {
        viewModel { SplashViewModel(get(), get(), get()) }
        scoped { GetSignedUser(get())}
    }

    scope(named<CreatePhotoItemFragment>()) {
        viewModel { CreatePhotoItemViewModel(get()) }
    }
}