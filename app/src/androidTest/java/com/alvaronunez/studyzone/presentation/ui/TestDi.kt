package com.alvaronunez.studyzone.presentation.ui

import android.os.Handler
import com.alvaronunez.studyzone.data.source.AuthenticationDataSource
import com.alvaronunez.studyzone.data.source.RemoteDataSource
import com.alvaronunez.studyzone.domain.Category
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.domain.User
import com.alvaronunez.studyzone.presentation.dataModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initMockedDiUi(vararg modules: Module) {
    startKoin {
        modules(listOf(mockedAppModule, dataModule) + modules)
    }
}

private val mockedAppModule = module {
    factory<RemoteDataSource>{ FakeRemoteDataSource1() }
    factory<AuthenticationDataSource>{ FakeFirebaseAuthDataSource1() }
    single<CoroutineDispatcher> { Dispatchers.Main }
    single { Handler() }
}

private val mockedItem = Item(
    "0",
    "Title",
    "Description for the item",
    null,
    "0",
    "0"
)

val defaultFakeItems1 = listOf(
    mockedItem.copy("1"),
    mockedItem.copy("2"),
    mockedItem.copy("3"),
    mockedItem.copy("4")
)

private val mockedCategory = Category(
    "0",
    "title",
    "Color",
    false
)

private val defaultFakeCategories = listOf(
    mockedCategory.copy("1"),
    mockedCategory.copy("2"),
    mockedCategory.copy("3"),
    mockedCategory.copy("4")
)

private val mockedUser = User(
    "0",
    "Name",
    "Last Name",
    "email@email.com"
)

class FakeRemoteDataSource1 : RemoteDataSource {

    override suspend fun getItemsByUser(userId: String): List<Item> =
        defaultFakeItems1

    override suspend fun getCategoriesByUser(userId: String): List<Category> =
        defaultFakeCategories

    override suspend fun addItem(item: Item): Boolean = true

    override suspend fun saveUser(user: User): Boolean = true

}

class FakeFirebaseAuthDataSource1 : AuthenticationDataSource {
    override fun getSignedUser(): User? =
        mockedUser

    override fun signOut() = Unit

    override suspend fun signInWithEmailAndPassword(email: String, password: String): User? =
        mockedUser

    override suspend fun signInWithGoogleCredential(token: String): User? =
        mockedUser

    override suspend fun signUpNewUser(
        email: String,
        password: String,
        displayName: String
    ): User? = mockedUser

    override fun removeSignedUser() = Unit

}