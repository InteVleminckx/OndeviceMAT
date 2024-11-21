package com.ondevice.offdevicetesting

import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import com.ondevice.mat.automation.Interactor
import kotlinx.coroutines.delay
import kotlin.math.log

class NewsTest : Test() {

    override val packageName = "kmp.news.app"

    init {
        appName = "NewsApp"
    }

    override suspend fun runTests() {
        super.runTests()
        executeTest(::fullNewsAppTest, 1, "News App Automation Test", false)
    }
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
    private suspend fun fullNewsAppTest(iterations: Int): Pair<Boolean, String> {
        for (i in 0 until iterations) {
            clickToFirstNew()
            navigateToSearch()
            for (i in countries) {
                search(i)
                delay(1000)
            }
            navigateToHome()
        }
        return Pair(true, "Successfully completed the News App test.")
    }

    private suspend fun clickToFirstNew():Pair<Boolean, String> {
        val target = Pair("Breaking News", Engine.searchTypes.TEXT)
        val firstNews = engine?.findObjectByClassName("android.view.View")?.get(6)
        val isFirstNewsClicked = firstNews?.let {
            engine?.click(it, target)
        } == true
        if(!isFirstNewsClicked) {
            logToFile("Clicking to first news was unsuccessful")
            return Pair(true, "Failed to click first news")
        }
        engine?.pressBack(target)
        logToFile("Successfully clicked to first new")
        return Pair(true, "Successfully clicked to first view")
    }

    private suspend fun navigateToSearch(): Pair<Boolean, String> {
        val target = Pair("Discover", Engine.searchTypes.TEXT)
        val searchNavBarButton = engine?.retrieveNode("Search", Engine.searchTypes.CONTENT_DESC)
        val isClickedToSearchNavBarButton = searchNavBarButton?.let {
            engine?.click(it, target)
        } == true
        if(!isClickedToSearchNavBarButton) {
            logToFile("Failed to click search nav bar button")
            return Pair(false, "failed ot navigate to search")
        }
        logToFile("Successfully navigated to search")
        return Pair(true, "Successfully navigated to search")
    }

    private suspend fun search(text: String): Pair<Boolean, String> {
        val searchTextField = engine?.findObjectByClassName("android.widget.EditText")?.get(0)
        val isSearchedFilled = searchTextField?.let {
            engine?.fillTextBox(it, text, text, Engine.searchTypes.TEXT)
        } == true
        if(!isSearchedFilled) {
            logToFile("Unsuccessful to fill the search field")
            return Pair(false, "Failed to fill the field")
        }

        logToFile("Successfully searched the word: $text")
        return Pair(true, "Successfully searched the word: $text")
    }

    private suspend fun saveNews(): Pair<Boolean, String> {
        val saveButton = engine?.findObjectByClassName("android.view.View")?.get(8)
        val target = Pair("Discover", Engine.searchTypes.TEXT)
        val isSavePressed = saveButton?.let {
            engine?.click(it, target)
        } == true
        if(!isSavePressed) {
            logToFile("Failed to save news")
            return Pair(false, "Failed to save news")
        }

        logToFile("Successfully saved a new")
        return Pair(true, "Successfully saved a new")
    }

    private suspend fun navigateToNews(): Pair<Boolean, String> {
        val savedNavButton = engine?.retrieveNode("Saved", Engine.searchTypes.CONTENT_DESC)
        val target = Pair("Saved News", Engine.searchTypes.TEXT)
        val isSavedButtonClicked = savedNavButton?.let {
            engine?.click(it, target)
        } == true

        if(!isSavedButtonClicked) {
            logToFile("Navigated to saved news")
            return Pair(false, "Failed to navigate to saved news")
        }

        logToFile("Successfully navigated to news")
        return Pair(true, "Successfully navigated to news")
    }

    private suspend fun navigateToHome(): Pair<Boolean, String> {
        val homeNavButton = engine?.retrieveNode("News", Engine.searchTypes.CONTENT_DESC)
        val target = Pair("Breaking News", Engine.searchTypes.TEXT)
        val isHomeButtonClicked = homeNavButton?.let {
            engine?.click(it, target)
        } == true

        if(!isHomeButtonClicked) {
            logToFile("Failed to navigate to home")
            return Pair(false, "Failed to navigate to home")
        }

        logToFile("Successfully navigated to home page")
        return Pair(true, "Successfully navigated to home")
    }
}
