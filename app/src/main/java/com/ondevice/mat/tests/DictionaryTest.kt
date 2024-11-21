package com.ondevice.offdevicetesting

import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import kotlinx.coroutines.delay

class DictionaryTest : Test() {

    override val packageName = "com.ss.dictionaryapp"

    init {
        appName = "DictionaryApp"
    }

    override suspend fun runTests() {
        super.runTests()
        executeTest(::fullDictionaryAppTest, 1, "Dictionary App Automation Test", false)
    }

    private suspend fun fullDictionaryAppTest(iterations: Int): Pair<Boolean, String> {
        val wordsToSearch = listOf("word", "baby", "love", "sun", "moon", "sky", "flower", "tree", "rain", "wind", "light")

        for (i in 0 until iterations) {
            logToFile("Iteration ${i + 1}")

            wordsToSearch.forEach { word ->
                val searchResult = searchAWord(word)
                if (!searchResult.first) return searchResult
            }
        }

        return Pair(true, "Successfully completed the Dictionary app test.")
    }

    private suspend fun searchAWord(word: String): Pair<Boolean, String> {
        logToFile("Searching for word: $word")

        val searchField = engine?.findObjectByClassName("android.widget.EditText")?.get(0)
            ?: return Pair(false, "Failed to retrieve search field")
        val filledSearchField = engine?.fillTextBox(searchField, word, word, Engine.searchTypes.TEXT) == true
        logToFile("fill text box")
        if (!filledSearchField) {
            logToFile("Failed to fill search field")
            return Pair(false, "Could not fill the search field with word: $word")
        }
        val searchButtonContentDesc = "Search a word"
        val searchButton = engine?.retrieveNode(searchButtonContentDesc, Engine.searchTypes.CONTENT_DESC, allNodes = true)
            ?: return Pair(false, "Failed to retrieve search button with content description $searchButtonContentDesc")

        logToFile("Retrieve search")

        val clickedSearchButton = engine?.click(searchButton, Pair(searchButtonContentDesc, Engine.searchTypes.CONTENT_DESC), ignoreEvent = true) == true
        if (!clickedSearchButton) {
            logToFile("Failed to click search button")
            return Pair(false, "Could not click the search button for word: $word")
        }

        logToFile("Successfully searched for word: $word")
        return Pair(true, "Successfully searched for word: $word")
    }
}
