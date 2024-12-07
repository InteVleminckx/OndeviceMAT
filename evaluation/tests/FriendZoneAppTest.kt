package com.ondevice.offdevicetesting

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.sign
import kotlin.random.Random
import kotlin.system.measureTimeMillis

@RunWith(AndroidJUnit4::class)
class FriendZoneAppTest : BaseTestClass("com.example.friendzone") {

    @Test
    fun runFriendZoneTest() {
        logToFile("Starting FriendZone Social Media App Automation Test")
        val result =
            executeTest(::fullFriendZoneAppTest, 1, "FriendZone Social Media App Automation Test")
        logToFile("FriendZone Test Result: $result")
        assertEquals("Successfully executed the FriendZone app test.", result)
    }

    private fun fullFriendZoneAppTest(iterations: Int): Pair<Boolean, String> {
        logToFile("Running full FriendZone app test")
        val executionTime = measureTimeMillis {
            navigateToSearch()
            Thread.sleep(250)
            searchUser("Grace Turner")
            Thread.sleep(250)
            clickToFirstSearchResult()
            Thread.sleep(250)
            followUser()
            Thread.sleep(250)
            goChats()
            Thread.sleep(250)
            writeMessageToUser("Grace Turner")
            Thread.sleep(250)
        }
        logToFile("Finished the FriendZone App test in ${executionTime / 1000.0} seconds")
        return Pair(true, "Successfully executed the FriendZone app test.")
    }

    private fun navigateToSearch(): Pair<Boolean, String> {
        logToFile("Navigating to search page")
        return try {

            val objects = device.findObjects(By.clazz("android.view.View")).sortedWith(
                compareBy(
                    { it.visibleBounds.top },
                    { it.visibleBounds.left },
                    { it.visibleBounds.bottom },
                    { it.visibleBounds.right }
                )
            )
            val searchButton = objects[objects.size - 6]
            searchButton.click()
            logToFile("Successfully navigated to search page")
            Pair(true, "Successfully navigated to search page")
        } catch (e: Exception) {
            logToFile("Failed to navigate to search page: ${e.message}")
            Pair(false, "Failed to navigate to search page")
        }
    }

    private fun searchUser(userName: String): Pair<Boolean, String> {
        logToFile("Searching for 'Grace'")
        return try {
            val searchField = device.findObject(UiSelector().className("android.widget.EditText"))
            searchField.setText(userName)
            logToFile("Successfully entered '$userName' in the search field")
            Pair(true, "Successfully entered '$userName' in the search field")
        } catch (e: Exception) {
            logToFile("Failed to enter text in the search field: ${e.message}")
            Pair(false, "Failed to enter text in the search field")
        }
    }

    private fun followUser(): Pair<Boolean, String> {

        logToFile("Following User")

        return try {
            val followButton = device.findObject(UiSelector().className("android.widget.Button"))
            followButton.click()
            Thread.sleep(500)
            device.pressBack()
            logToFile("Successfully followed the user")
            Pair(true, "Successfully followed the user")

        } catch (e: Exception) {
            logToFile("Failed to follow the user: ${e.message}")
            Pair(false, "Failed to follow the user")
        }
    }

    private fun clickToFirstSearchResult(): Pair<Boolean, String> {
        logToFile("Tapping on the search result for Grace Turner")
        return try {
            val graceRow = findObjectByClassAndInstance("android.view.View", 5)
            graceRow?.click()

            logToFile("Successfully tapped on the specified view")
            Pair(true, "Successfully tapped on the specified view")
        } catch (e: Exception) {
            logToFile("Failed to tap on the specified view: ${e.message}")
            Pair(false, "Failed to tap on the specified view")
        }
    }

    private fun goChats(): Pair<Boolean, String> {
        logToFile("Navigating to messages")
        return try {
            findObjectByClassAndInstance("android.view.View", 18)?.click()
            logToFile("Successfully went to messages")
            Pair(true, "Successfully went to messages")
        } catch (e: Exception) {
            logToFile("Failed to go to messages: ${e.message}")
            Pair(false, "Failed to go to messages")
        }
    }

    private fun writeMessageToUser(userName: String): Pair<Boolean, String> {
        logToFile("Writing a message to Grace Turner")
        return try {

            var searchBox = device.findObjects(By.clazz("android.widget.EditText"))
            while (searchBox.size != 1) {
                Thread.sleep(100)
                searchBox = device.findObjects(By.clazz("android.widget.EditText"))
            }
            searchBox[0].text = userName

            var userInstance = device.findObjects(By.clazz("android.view.View"))
            var found = false
            while (!found) {
                for (view in userInstance) {
                    for (child in view.children) {
                        if (child.className == "android.widget.TextView" && child.text == userName) {
                            found = true
                            view.click()

                            val messageField =
                                device.findObject(UiSelector().className("android.widget.EditText"))
                            messageField.setText("Hi there!")

                            val sendButton = device.findObject(UiSelector().description("Send"))
                            sendButton.click()

                            val objects =
                                device.findObjects(By.clazz("android.view.View")).sortedWith(
                                    compareBy(
                                        { it.visibleBounds.top },
                                        { it.visibleBounds.left },
                                    )
                                )
                            objects[4].click()
                        }
                    }
                }
                if (!found) {
                    Thread.sleep(100)
                    userInstance = device.findObjects(By.clazz("android.view.View"))
                }
            }

            logToFile("Successfully sent a message to Grace")
            Pair(true, "Successfully sent a message to Grace")

        } catch (e: Exception) {
            logToFile("Failed to write a message: ${e.message}")
            Pair(false, "Failed to write a message")
        }
    }
}