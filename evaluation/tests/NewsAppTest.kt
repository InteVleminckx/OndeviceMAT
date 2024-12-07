package com.ondevice.offdevicetesting

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.UiSelector
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.math.log
import kotlin.system.measureTimeMillis


@RunWith(AndroidJUnit4::class)
class NewsAppTest : BaseTestClass("kmp.news.app") {

    private var countries: List<String> = mutableListOf(
        "United States",
        "Canada",
        "Mexico",
        "Brazil",
        "India",
        "Turkey",
        "Korea",
        "Thailand"
    )

    @Test
    fun runNewsAppTest() {
        logToFile("Starting News App Automation Test")
        val result = executeTest(::fullNewsAppTest, 1, "News App Automation Test")
        logToFile("News App Test Result: $result")
        assertEquals("Successfully executed the news app test.", result)
    }

    private fun fullNewsAppTest(iterations: Int): Pair<Boolean, String> {
        logToFile("Running full news app test")
        val executionTime = measureTimeMillis {
            clickToFirstNews()
            navigateToSearch()
            search()
            navigateToHome()
        }
        logToFile("Finished the News App test in ${executionTime / 1000.0} seconds")
        return Pair(true, "Successfully executed the news app test.")
    }

    private fun clickToFirstNews(): Pair<Boolean, String> {
        logToFile("Clicking to first news")
        try {
            clickSpecificView(6)
            return Pair(true, "Clicked to first element")
        } catch (e: Exception) {
            logToFile("Failed to click first news")
            return Pair(false, "Failed to click first news")
        }
    }

    private fun navigateToSearch(): Pair<Boolean, String> {
        logToFile("Navigating to search")
        try {
            val searchNavBarMenuButton =
                device.findObject(UiSelector().descriptionContains("Search"))
            searchNavBarMenuButton.click()
            return Pair(true, "success to navigate search")
        } catch (e: Exception) {
            return Pair(false, "Failed to go search")
        }
    }

    private fun clickSpecificView(instanceNumber: Int): Pair<Boolean, String> {
        logToFile("Attempting to click specific view with instance number $instanceNumber")
        repeat(3) { attempt ->
            try {
                device.waitForIdle(timeout)
                val obj = findObjectByClassAndInstance("android.view.View", instanceNumber)
                if (obj != null) {
                    obj.click()
                    logToFile("Successfully clicked on the specific instance of android.view.View")
                    return Pair(
                        true,
                        "Successfully clicked on the specific instance of android.view.View"
                    )
                } else {
                    logToFile("Object found but not interactable. Retrying...")
                }
            } catch (e: Exception) {
                logToFile("Attempt $attempt failed with exception: ${e.message}")
                restartUiAutomationService()
                Thread.sleep(5000)
            }
        }
        logToFile("Failed to click on the specific instance of android.view.View after retries")
        return Pair(
            false,
            "Failed to click on the specific instance of android.view.View after retries"
        )
    }

    private fun search(): Pair<Boolean, String> {
        logToFile("Searching")
        try {
            for (i in countries) {
                fillEditText(i)
                Thread.sleep(1500)
            }
            return Pair(true, "Success to search")
        } catch (e: Exception) {
            return Pair(false, "Failed to search")
        }
    }

    private fun fillEditText(text: String): Pair<Boolean, String> {
        logToFile("Filling EditText with text: $text")
        return try {
            val editText = device.findObject(UiSelector().className("android.widget.EditText"))
            editText.setText(text)
            logToFile("Successfully filled EditText with text: $text")
            Pair(true, "Successfully filled EditText with text: $text")
        } catch (e: Exception) {
            logToFile("Failed to fill EditText: ${e.message}")
            Pair(false, "Failed to fill EditText with text: $text")
        }
    }

    private fun restartUiAutomationService() {
        logToFile("Restarting UiAutomation service...")
        try {
            device.executeShellCommand("am force-stop com.android.commands.uiautomator")
            device.executeShellCommand("am start com.android.commands.uiautomator/.UiAutomatorRunner")
            logToFile("Successfully restarted UiAutomation service")
        } catch (e: Exception) {
            logToFile("Failed to restart UiAutomation service: ${e.message}")
        }
    }

    private fun navigateToHome(): Pair<Boolean, String> {
        logToFile("Navigating to home/news page")
        return try {
            val newsTab = device.findObject(UiSelector().text("News"))
            newsTab.click()
            logToFile("Successfully navigated to News page")
            Pair(true, "Successfully navigated to News page")
        } catch (e: Exception) {
            logToFile("Failed to navigate to News page: ${e.message}")
            Pair(false, "Failed to navigate to News page")
        }
    }

    private fun navigateToSaved(): Pair<Boolean, String> {
        logToFile("Navigating to saved")
        return try {
            val newsTab = device.findObject(UiSelector().text("Saved"))
            newsTab.click()
            logToFile("Successfully navigated to Saved page")
            Pair(true, "Successfully navigated to News page")
        } catch (e: Exception) {
            logToFile("Failed to navigate to Saved page: ${e.message}")
            Pair(false, "Failed to navigate to Saved page")
        }
    }

    private fun saveNews(): Pair<Boolean, String> {
        logToFile("Saving news")
        try {
            clickSpecificView(8)
            return Pair(true, "Success to save news")
        } catch (e: Exception) {
            return Pair(false, "failed to save news")
        }
    }

    private fun newsGestureScrollDown(): Pair<Boolean, String> {
        logToFile("Scrolling down on home")
        return try {
            // Navigate to the News tab
            val newsTab = device.findObject(UiSelector().text("News"))
            newsTab.click()
            logToFile("Successfully navigated to News page")

            // Perform a swipe gesture to scroll down
            val startX = device.displayWidth / 2
            val startY = (device.displayHeight * 0.8).toInt()
            val endY = (device.displayHeight * 0.2).toInt()
            device.swipe(startX, startY, startX, endY, 20)
            logToFile("Successfully swiped down on News page")
            Pair(true, "Successfully swiped down on News page")
        } catch (e: Exception) {
            logToFile("Failed to navigate or swipe on News page: ${e.message}")
            Pair(false, "Failed to navigate or swipe on News page")
        }
    }


}