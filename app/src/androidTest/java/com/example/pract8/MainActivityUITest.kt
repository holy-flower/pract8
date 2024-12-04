package com.example.pract8

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testUrlEditText_isDisplayed() {
        onView(withId(R.id.urlEditText)).check(matches(isDisplayed()))
    }

    @Test
    fun testDownloadButton_isDisplayed() {
        onView(withId(R.id.downloadButton)).check(matches(isDisplayed()))
    }

    @Test
    fun checkEditTextInitialState() {
        onView(withId(R.id.urlEditText)).check(matches(withText("")))
    }

    @Test
    fun checkButtonText() {
        onView(withId(R.id.downloadButton)).check(matches(withText("Download Image")))
    }

    @Test
    fun testValidUrlTriggersDownload() {
        onView(withId(R.id.urlEditText)).perform(typeText("https://avatars.mds.yandex.net/i?id=02a0d438915e4409b6779abb9faf64f6cfcca7e5-5380211-images-thumbs&n=13"), closeSoftKeyboard())
        onView(withId(R.id.downloadButton)).perform(click())

        Thread.sleep(5000)
        // Проверка, что изображение загружается и отображается
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }
}