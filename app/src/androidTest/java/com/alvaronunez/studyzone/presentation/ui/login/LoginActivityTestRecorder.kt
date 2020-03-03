package com.alvaronunez.studyzone.presentation.ui.login

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.ui.initMockedDiUi
import com.alvaronunez.studyzone.usecases.SignInWithEmailAndPassword
import com.alvaronunez.studyzone.usecases.SignInWithGoogleCredential
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTestRecorder : AutoCloseKoinTest() {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java, false, false)

    @Before
    fun setUp() {
        stopKoin()
        val testModule = module(override = true) {
            scope(named<LoginActivity>()) {
                viewModel { LoginViewModel(get(), get(), get()) }
                scoped { SignInWithEmailAndPassword(get()) }
                scoped { SignInWithGoogleCredential(get()) }
            }
        }
        initMockedDiUi(testModule)
    }

    @Test
    fun loginActivityTestRecorder() {
        mActivityTestRule.launchActivity(null)
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