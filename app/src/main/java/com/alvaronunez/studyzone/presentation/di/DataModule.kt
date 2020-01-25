package com.alvaronunez.studyzone.presentation.di

import com.alvaronunez.studyzone.data.repository.AuthenticationRepository
import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.data.source.AuthenticationDataSource
import com.alvaronunez.studyzone.data.source.RemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun repositoryProvider(remoteDataSource: RemoteDataSource) = Repository(remoteDataSource)

    @Provides
    fun authenticationRepositoryProvider(authenticationDataSource: AuthenticationDataSource) = AuthenticationRepository(authenticationDataSource)
}