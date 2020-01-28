package com.alvaronunez.studyzone.data.repository

import com.alvaronunez.studyzone.data.source.RemoteDataSource
import com.alvaronunez.studyzone.domain.User
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = Repository(remoteDataSource)
    }

    @Test
    fun `getItemsByUser calls remote data source`() {
        runBlocking {
            repository.getItemsByUser(mockedUser.id)

            verify(remoteDataSource).getItemsByUser(mockedUser.id)
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