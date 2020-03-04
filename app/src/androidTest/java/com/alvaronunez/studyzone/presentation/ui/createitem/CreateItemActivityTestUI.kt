package com.alvaronunez.studyzone.presentation.ui.createitem

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.alvaronunez.studyzone.R
import org.junit.Rule
import org.junit.Test

class LoginActivityTestRecorder {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(CreateItemActivity::class.java, false, false)

    @Test
    fun checkViewComponents() {
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.itemTitle))
            .check(matches(isDisplayed()))
        onView(withId(R.id.itemDescription))
            .check(matches(isDisplayed()))
        onView(withId(R.id.add_item_button))
            .check(matches(isDisplayed()))
    }
}
