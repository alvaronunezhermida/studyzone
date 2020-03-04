package com.alvaronunez.studyzone.presentation.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.alvaronunez.studyzone.R
import org.junit.Rule
import org.junit.Test

class MainActivityTestRecorder {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Test
    fun checkViewComponent2() {
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.fab))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fab))
            .perform(click())
        Thread.sleep(800)
        onView(withId(R.id.fab_item))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fab_image))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fab_file))
            .check(matches(isDisplayed()))
        onView(withId(R.id.fab_audio))
            .check(matches(isDisplayed()))
    }
}
