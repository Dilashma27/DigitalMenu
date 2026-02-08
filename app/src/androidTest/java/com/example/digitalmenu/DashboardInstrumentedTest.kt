package com.example.digitalmenu

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<DashboardActivity>()

    @Test
    fun dashboard_navigate_to_home_and_order() {

        // Home tab (default screen)
        composeRule.onNodeWithText("Home")
            .performClick()

        // Order tab
        composeRule.onNodeWithText("Order")
            .performClick()
    }
}
