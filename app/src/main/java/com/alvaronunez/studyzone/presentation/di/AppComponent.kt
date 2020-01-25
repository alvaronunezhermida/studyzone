package com.alvaronunez.studyzone.presentation.di

import android.app.Application
import com.alvaronunez.studyzone.presentation.ui.createitem.CreateItemActivityComponent
import com.alvaronunez.studyzone.presentation.ui.createitem.CreateItemActivityModule
import com.alvaronunez.studyzone.presentation.ui.login.LoginActivityComponent
import com.alvaronunez.studyzone.presentation.ui.login.LoginActivityModule
import com.alvaronunez.studyzone.presentation.ui.main.MainActivityComponent
import com.alvaronunez.studyzone.presentation.ui.main.MainActivityModule
import com.alvaronunez.studyzone.presentation.ui.signup.SignUpActivityComponent
import com.alvaronunez.studyzone.presentation.ui.signup.SignUpActivityModule
import com.alvaronunez.studyzone.presentation.ui.splash.SplashActivityComponent
import com.alvaronunez.studyzone.presentation.ui.splash.SplashActivityModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {

    fun plus(module: CreateItemActivityModule): CreateItemActivityComponent
    fun plus(module: MainActivityModule): MainActivityComponent
    fun plus(module: SplashActivityModule): SplashActivityComponent
    fun plus(module: LoginActivityModule): LoginActivityComponent
    fun plus(module: SignUpActivityModule): SignUpActivityComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }

}