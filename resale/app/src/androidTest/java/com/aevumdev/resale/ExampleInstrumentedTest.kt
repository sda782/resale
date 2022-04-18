package com.aevumdev.resale

import android.widget.SeekBar
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.Matchers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    @JvmField
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = getInstrumentation().targetContext
        assertEquals("com.aevumdev.resale", appContext.packageName)
    }

    @Test
    fun loginTest(){
        onView(withId(R.id.fab)).perform(click())
        onView(withText("Login with Email")).check(matches(isDisplayed()))
        onView(withHint("email")).perform(typeText("a@a.com"))
        onView(withHint("password")).perform(typeText("123456"))
        onView(withText("Login")).perform(click())
        //onView(withId(R.id.auth_status)).check(matches(withText(R.string.auth_status_logged_in)))
    }
    @Test
    fun signoutTest(){
        val appContext = getInstrumentation().targetContext
        openActionBarOverflowOrOptionsMenu(appContext)
        onView(withText(R.string.logout)).perform(click())
        //onView(withId(R.id.auth_status)).check(matches(withText(R.string.auth_status_logged_out)))
    }
}