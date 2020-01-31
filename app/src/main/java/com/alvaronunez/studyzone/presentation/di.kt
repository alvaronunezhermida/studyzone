package com.alvaronunez.studyzone.presentation

import android.app.Application
import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.data.source.AuthenticationDataSource
import com.alvaronunez.studyzone.data.source.RemoteDataSource
import com.alvaronunez.studyzone.presentation.data.FirebaseAuthDataSource
import com.alvaronunez.studyzone.presentation.data.FirebaseDataSource
import com.alvaronunez.studyzone.presentation.ui.createitem.CreateItemActivity
import com.alvaronunez.studyzone.presentation.ui.createitem.CreateItemViewModel
import com.alvaronunez.studyzone.presentation.ui.createphotoitem.CreatePhotoItemActivity
import com.alvaronunez.studyzone.presentation.ui.createphotoitem.CreatePhotoItemViewModel
import com.alvaronunez.studyzone.presentation.ui.login.LoginActivity
import com.alvaronunez.studyzone.presentation.ui.login.LoginViewModel
import com.alvaronunez.studyzone.presentation.ui.main.MainActivity
import com.alvaronunez.studyzone.presentation.ui.main.MainViewModel
import com.alvaronunez.studyzone.presentation.ui.signup.SignUpActivity
import com.alvaronunez.studyzone.presentation.ui.signup.SignUpViewModel
import com.alvaronunez.studyzone.presentation.ui.splash.SplashActivity
import com.alvaronunez.studyzone.presentation.ui.splash.SplashViewModel
import com.alvaronunez.studyzone.usecases.*
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
}

private val dataModule = module {
    factory { Repository(get()) }
    factory { AuthenticationRepository(get()) }
}

private val scopesModule = module {
    scope(named<CreateItemActivity>()) {
        viewModel { CreateItemViewModel(get(), get(), get()) }
        scoped { GetCategoriesByUser(get())}
        scoped { GetSignedUser(get()) }
        scoped { AddItem(get()) }
    }

    scope(named<LoginActivity>()) {
        viewModel { LoginViewModel(get(), get()) }
        scoped { SignInWithEmailAndPassword(get())}
        scoped { SignInWithGoogleCredential(get()) }
    }

    scope(named<MainActivity>()) {
        viewModel { MainViewModel(get(), get(), get()) }
        scoped { GetItemsByUser(get())}
        scoped { GetSignedUser(get()) }
        scoped { SignOutSignedUser(get()) }
    }

    scope(named<SignUpActivity>()) {
        viewModel { SignUpViewModel(get(), get(), get()) }
        scoped { SaveUser(get())}
        scoped { SignUpNewUser(get()) }
        scoped { RemoveSignedUser(get()) }
    }

    scope(named<SplashActivity>()) {
        viewModel { SplashViewModel(get()) }
        scoped { GetSignedUser(get())}
    }

    scope(named<CreatePhotoItemActivity>()) {
        viewModel { CreatePhotoItemViewModel() }
    }
}