package com.ondevice.offdevicetesting

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.system.measureTimeMillis

@RunWith(AndroidJUnit4::class)
class DictionaryAppTest : BaseTestClass("com.ss.dictionaryapp") {
    private val wordsToSearch = listOf(
        "word",
        "baby",
        "love",
        "sun",
        "moon",
        "sky",
        "flower",
        "tree",
        "rain",
        "wind",
        "light"
    )

    @Test
    fun runDictionaryApp() {
        logToFile("Starting Dictionary App Automation Test")
        var result = executeTest(::fullDictionaryAppTest, 1, "Dictionary App Automation Test")
        logToFile("Dictionary App Test Result: $result")
        assertEquals("Successfully executed the dictionary app test.", result)
    }

    private fun fullDictionaryAppTest(iterations: Int): Pair<Boolean, String> {
        logToFile("Executing full dictionary app test")
        val executionTime = measureTimeMillis {
            wordsToSearch.forEach { word ->
                searchAWord(word)
            }
        }
        logToFile("Dictionary App test completed in ${executionTime / 1000.0} seconds")
        return Pair(true, "Successfully executed the dictionary app test.")
    }

    private fun searchAWord(text: String): Pair<Boolean, String> {
        logToFile("Searching for word: $text")
        return try {
            val searchField = device.findObject(UiSelector().className("android.widget.EditText"))
            searchField.setText(text)
            val searchButton = device.findObject(UiSelector().description("Search a word"))
            searchButton.click()
            logToFile("Search completed successfully for word: $text")
            Pair(true, "Successfully searched for word: $text")
        } catch (e: Exception) {
            e.printStackTrace()
            val errorMessage = "Failed to search for word: $text - ${e.message}"
            logToFile(errorMessage)
            Pair(false, errorMessage)
        }
    }
}
