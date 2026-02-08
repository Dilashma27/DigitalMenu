package com.example.digitalmenu

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.example.digitalmenu.view.LoginActivity
import com.example.digitalmenu.view.RegistrationActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<RegistrationActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testRegisterButton_navigatesToLogin() {

        // Enter email
        composeRule.onNodeWithTag("email")
            .performTextInput("ram@gmail.com")

        // Enter password
        composeRule.onNodeWithTag("password")
            .performTextInput("password")

        // Click Register button
        composeRule.onNodeWithTag("register")
            .performClick()

        // Verify navigation to LoginActivity
        Intents.intended(hasComponent(LoginActivity::class.java.name))
    }
}
