package com.example.pract8

import android.graphics.BitmapFactory
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testCalculateInSampleSize() {
        val options = BitmapFactory.Options()
        options.outHeight = 1000
        options.outWidth = 1000

        activityRule.scenario.onActivity { activity ->
            val inSampleSize = activity.calculateInSampleSize(options, 500, 500)
            assertEquals(2, inSampleSize)
        }
    }
}
