package com.alvaronunez.studyzone.presentation.di

import com.alvaronunez.studyzone.data.source.AuthenticationDataSource
import com.alvaronunez.studyzone.data.source.RemoteDataSource
import com.alvaronunez.studyzone.presentation.data.FirebaseAuthDataSource
import com.alvaronunez.studyzone.presentation.data.FirebaseDataSource
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun remoteDataSourceProvider(): RemoteDataSource = FirebaseDataSource()

    @Provides
    fun authenticationDataSourceProvider(): AuthenticationDataSource = FirebaseAuthDataSource()

}