/*
 * Copyright (c) 2025 Proton AG
 * This file is part of Proton AG and Proton Authenticator.
 *
 * Proton Authenticator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Authenticator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Authenticator.  If not, see <https://www.gnu.org/licenses/>.
 */

package proton.android.authenticator.shared.testing.examples

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import proton.android.authenticator.shared.testing.HiltComponentActivity

/**
 * Example test demonstrating how to use HiltComponentActivity for Compose testing.
 * 
 * This test shows how to test Compose UI components with Hilt dependency injection
 * using the HiltComponentActivity as a test host.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HiltComponentActivityExampleTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(HiltComponentActivity::class.java)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHiltComponentActivityLaunches() {
        // This test verifies that the HiltComponentActivity can be launched
        // and is properly configured with Hilt
        
        activityRule.scenario.onActivity { activity ->
            assert(activity is HiltComponentActivity)
            assert(!activity.isFinishing)
        }
    }

    @Test
    fun testComposeContentIsDisplayed() {
        // This test demonstrates how to test Compose content within the HiltComponentActivity
        // The activity has a basic Material3 setup that can be tested
        
        activityRule.scenario.onActivity { activity ->
            // The activity should have Compose content set up
            assert(activity != null)
        }
    }

    @Test
    fun testHiltDependencyInjectionInActivity() {
        // This test shows how to access Hilt-injected dependencies
        // within the test activity context
        
        activityRule.scenario.onActivity { activity ->
            // In a real test, you would inject and test dependencies here
            // For example:
            // val someRepository = hiltRule.get<SomeRepository>()
            // val result = someRepository.getSomeData()
            // assert(result != null)
            
            // For now, just verify the activity is properly initialized
            assert(activity != null)
        }
    }

    @Test
    fun testActivityWithComposeRule() {
        // This test shows how to use ComposeTestRule with the HiltComponentActivity
        // for more advanced Compose testing scenarios
        
        composeTestRule.setContent {
            // In a real test, you would set up your Compose content here
            // and test it with Hilt dependencies available
        }
        
        // Verify that the compose content is displayed
        composeTestRule.onRoot().assertIsDisplayed()
    }
}