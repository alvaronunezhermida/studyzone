package com.alvaronunez.studyzone.presentation.ui.login

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.data.source.AuthenticationDataSource
import com.alvaronunez.studyzone.data.source.RemoteDataSource
import com.alvaronunez.studyzone.domain.Category
import com.alvaronunez.studyzone.domain.Item
import com.alvaronunez.studyzone.domain.User
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTestRecorder {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        val testModule = module(override = true) {
            factory<RemoteDataSource> { FakeRemoteDataSource() }
            factory<AuthenticationDataSource> { FakeFirebaseAuthDataSource() }
        }
        loadKoinModules(testModule)
    }

    @Test
    fun loginActivityTestRecorder() {
        val button = Espresso.onView(
            Matchers.allOf(
                withId(R.id.login),
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    4
                ),
                ViewMatchers.isDisplayed()
            )
        )
        button.check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    companion object {
        private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int
        ): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun describeTo(description: Description) {
                    description.appendText("Child at position $position in parent ")
                    parentMatcher.describeTo(description)
                }

                public override fun matchesSafely(view: View): Boolean {
                    val parent = view.parent
                    return (parent is ViewGroup && parentMatcher.matches(parent)
                            && view == parent.getChildAt(position))
                }
            }
        }
    }
}

private val mockedItem = Item(
    "0",
    "Title",
    "Description for the item",
    null,
    "0",
    "0"
)

val defaultFakeItems = listOf(
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

class FakeRemoteDataSource : RemoteDataSource {

    override suspend fun getItemsByUser(userId: String): List<Item> =
        defaultFakeItems

    override suspend fun getCategoriesByUser(userId: String): List<Category> =
        defaultFakeCategories

    override suspend fun addItem(item: Item): Boolean = true

    override suspend fun saveUser(user: User): Boolean = true

}

class FakeFirebaseAuthDataSource : AuthenticationDataSource {
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