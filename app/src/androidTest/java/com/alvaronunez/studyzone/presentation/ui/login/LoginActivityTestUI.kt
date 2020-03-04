package com.alvaronunez.studyzone.presentation.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.alvaronunez.studyzone.R
import org.junit.Rule
import org.junit.Test

class LoginActivityTestRecorder {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java, false, false)

    @Test
    fun checkViewComponents() {
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.login))
            .check(matches(isDisplayed()))
        onView(withId(R.id.etUserEmail))
            .check(matches(isDisplayed()))
        onView(withId(R.id.etPassword))
            .check(matches(isDisplayed()))
        onView(withId(R.id.login))
            .check(matches(isDisplayed()))
        onView(withId(R.id.google_login))
            .check(matches(isDisplayed()))
        onView(withId(R.id.sign_up))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkShowsMain() {
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.etUserEmail)).perform(typeText("test@gmail.com"))
        Thread.sleep(800)
        onView(withId(R.id.etPassword)).perform(typeText("111111"), closeSoftKeyboard())
        Thread.sleep(800)
        onView(withId(R.id.login)).perform(click())
        Thread.sleep(800)
        onView(withId(R.id.fab))
            .check(matches(isDisplayed()))
    }



    @Test
    fun checkShowsSignUp() {
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.sign_up)).perform(click())
        onView(withId(R.id.confirmPassword)).check(matches(isDisplayed()))
    }
}
