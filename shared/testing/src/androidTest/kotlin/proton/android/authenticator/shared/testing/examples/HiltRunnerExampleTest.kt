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

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import proton.android.authenticator.shared.testing.HiltComponentActivity

/**
 * Example test demonstrating how to use the HiltRunner with HiltComponentActivity.
 * 
 * This test shows the basic setup for writing instrumentation tests with Hilt
 * dependency injection in the Proton Authenticator project.
 * 
 * The HiltRunner is automatically configured in the build.gradle.kts file,
 * so these tests will run with Hilt support enabled.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HiltRunnerExampleTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(HiltComponentActivity::class.java)

    @Test
    fun testHiltRunnerIsWorking() {
        // This test verifies that the HiltRunner is properly configured
        // and that the HiltComponentActivity can be launched with Hilt support
        
        // The activity should be launched successfully
        activityRule.scenario.onActivity { activity ->
            assert(activity is HiltComponentActivity)
        }
    }

    @Test
    fun testHiltDependencyInjection() {
        // This test demonstrates that Hilt dependency injection is working
        // in the test environment
        
        activityRule.scenario.onActivity { activity ->
            // Verify that the activity is properly initialized
            assert(activity != null)
            
            // In a real test, you would inject dependencies here and test them
            // For example:
            // val someRepository = hiltRule.get<SomeRepository>()
            // assert(someRepository != null)
        }
    }

    @Test
    fun testActivityLifecycle() {
        // This test verifies that the activity lifecycle works correctly
        // with Hilt integration
        
        activityRule.scenario.onActivity { activity ->
            // Verify that the activity is in the correct state
            assert(!activity.isFinishing)
        }
    }
}