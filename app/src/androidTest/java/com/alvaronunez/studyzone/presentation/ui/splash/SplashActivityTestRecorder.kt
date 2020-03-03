package com.alvaronunez.studyzone.presentation.ui.splash


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.ui.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SplashActivityTestRecorder {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java, false, false)

    @Test
    fun loginHasRegisterOption() {
        mActivityTestRule.launchActivity(null)

        onView(withId(R.id.sign_up)).check(matches(withText("¿Todavía no tienes cuenta? Regístrate")))
    }

}
