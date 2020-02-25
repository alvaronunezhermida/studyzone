package com.alvaronunez.studyzone.usecases

import com.alvaronunez.studyzone.data.repository.Repository
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.domain.User
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetItemsByUserTest {

    @Mock
    lateinit var repository: Repository

    lateinit var getItemsByUser: GetItemsByUser

    @Before
    fun setUp() {
        getItemsByUser = GetItemsByUser(repository)
    }

    @Test
    fun `invoke calls repository`() {
        runBlocking {
            val items = listOf(mockedItem, mockedItem.copy(uid="1"))
            whenever(repository.getItemsByUser(mockedUser.id)).thenReturn(items)

            val result = getItemsByUser.invoke(mockedUser.id)

            assertEquals(items, result)
        }
    }

    private val mockedUser = User(
        "0",
        "Name",
        "Last Name",
        "email@email.com",
        "Display Name"
    )

    private val mockedItem = Item(
        "0",
        "Title",
        "Description for the item",
        null,
        "0",
        "0"
    )


}