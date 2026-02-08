package com.example.digitalmenu

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.onView
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import com.example.digitalmenu.DashboardActivity
import com.example.digitalmenu.view.LoginActivity
import com.example.digitalmenu.view.RegistrationActivity
import org.junit.After

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<LoginActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }
    @Test
    fun testSuccessfulLogin_navigatesToDashboard() {
        // Enter email
        composeRule.onNodeWithTag("email")
            .performTextInput("jharashmi3027@gmail.com")

        // Enter password
        composeRule.onNodeWithTag("password")
            .performTextInput("password")

        // Click Login
        composeRule.onNodeWithTag("login")
            .performClick()

        // Wait for navigation to DashboardActivity (wait for "Dashboard" title)
        composeRule.waitUntil(10000) {
            composeRule.onAllNodes(hasText("Dashboard")).fetchSemanticsNodes().isNotEmpty()
        }
        Intents.intended(hasComponent(DashboardActivity::class.java.name))

        // Assert that "Digital Menu" text is present on the Dashboard/Home screen
        composeRule.onNodeWithText("Digital Menu").assertExists()

        // Wait for a few seconds so the user can see the dashboard before the test closes
        Thread.sleep(5000)
    }
}