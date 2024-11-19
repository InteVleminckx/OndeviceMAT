package com.ondevice.mat.tests

import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import kotlin.math.log

class FriendzoneTest: Test() {
    override val packageName = "com.example.friendzone"
    private val users = listOf("Grace Turner", "Santosh", "Mohit Damke") // Changed to listOf for better Kotlin practices
    init {
        appName = "FriendZone"
    }

    override suspend fun runTests() {
        super.runTests()
        executeTest(::fullFriendzoneAppTest, 1, "FriendZone App Automation Test", false)
    }

    private suspend fun fullFriendzoneAppTest(iterations: Int): Pair<Boolean, String> {
        for (user in users) { // Iterate through the user list
            for (i in 0 until iterations) {
                logToFile("Starting test for user: $user")
                //signIn()
                var result = navigateToSearch()
                if (!result.first) return result
                result = searchUser(user)
                if (!result.first) return result
                result = clickToFirstSearchResult(user)
                if (!result.first) return result
                result = followUser()
                if (!result.first) return result
                result = goBack()
                if (!result.first) return result
                result = goChats()
                if (!result.first) return result
                result = writeMessageToUser(user)
                if (!result.first) return result
            }
        }
        return Pair(true, "Successfully completed the FriendZone App test for all users.")
    }
    private suspend fun signIn():Pair<Boolean, String> {
        logToFile("Trying to sign in")

        val mailField = engine?.findObjectByClassName("android.widget.EditText")?.get(0)
        val isMailFilled = mailField?.let {
            engine?.fillTextBox(mailField, "elf.prlk64@gmail.com", "elf.prlk64@gmail.com", Engine.searchTypes.TEXT)
        } == true
        if(!isMailFilled) {
            logToFile("Unsuccessful to fill mail field")
            return Pair(false, "Unsuccessfully to fill mail field")
        }
        val passwordField = engine?.findObjectByClassName("android.widget.EditText")?.get(1)
        val isPasswordFilled = passwordField?.let {
            engine?.fillTextBox(it, "kaburga9", "kaburga9", Engine.searchTypes.TEXT)
        } == true

        if(!isPasswordFilled) {
            logToFile("Unsuccessful to fill password field")
            return Pair(false, "Failed to fill password field")
        }
        val target = Pair("Hello, Elif", Engine.searchTypes.CONTENT_DESC)
        val loginButtonClicked = engine?.findObjectByClassName("android.widget.Button")?.get(0)
        val isLoginButtonClicked = loginButtonClicked?.let {
            engine?.click(it, target)
        } == true
        if(!isLoginButtonClicked) {
            logToFile("Failed to login")
            return Pair(false, "Failed to login")
        }
        logToFile("Successfully logged in")
        return Pair(true, "Successfully logged in")
    }

    private suspend fun navigateToSearch(): Pair<Boolean, String> {
        val searchButton = engine?.findObjectByClassName("android.view.View")?.get(21)
        val target = Pair("SEARCH", Engine.searchTypes.TEXT)
        val isSearchButtonClicked = searchButton?.let {
            engine?.click(it, target)
        } == true
        if(!isSearchButtonClicked) {
            logToFile("Failed to navigate search")
            return Pair(false, "Failed to navigate  search page")
        }
        logToFile("Successfully navigated to Search Page")
        return Pair(true, "Failed to navigate to search page")
    }

    private suspend fun searchUser(userName: String): Pair<Boolean, String> {
        logToFile("Starting to search for user: $userName")
        val searchField = engine?.findObjectByClassName("android.widget.EditText")?.get(0)
        val isSearchFilledWithText = searchField?.let {
            engine?.fillTextBox(it, userName, userName, Engine.searchTypes.TEXT)
        } == true

        if(!isSearchFilledWithText) {
            logToFile("Failed to fill user name in search field")
            return Pair(false, "Failed to fill search text field")
        }

        return Pair(true, "Successfully searched for $userName")
    }

    private suspend fun clickToFirstSearchResult(userName: String): Pair<Boolean, String> {
        logToFile("Opening first result on search")
        val target = Pair(userName, Engine.searchTypes.TEXT)
        val firstResultRow = engine?.findObjectByClassName("android.view.View")?.get(5)
        val isFirstResultClicked = firstResultRow?.let {
            engine?.click(it, target)
        } == true
        if(!isFirstResultClicked) {
            logToFile("Unsuccessful to click to first result")
            return Pair(false, "Failed to click to first result")
        }
        logToFile("Successfully clicked to first result")
        return Pair(true, "Successfully clicked to first user")
    }

    private suspend fun followUser(): Pair<Boolean, String> {
        logToFile("Following user")
        val target = Pair("Follow", Engine.searchTypes.TEXT)
        val followButton = engine?.findObjectByClassName("android.widget.Button")?.get(0)
        val isFollowButtonPressed = followButton?.let {
            engine?.click(it, target)
        } == true

        if(!isFollowButtonPressed) {
            logToFile("Failed to press to follow button")
            return Pair(false, "Failed to click follow button")
        }

        logToFile("Successfully followed user")
        return Pair(true, "Successfully followed ")
    }

    private suspend fun goBack(): Pair<Boolean, String> {
        val backButton = engine?.findObjectByClassName("android.view.View")?.get(8)
        val target = Pair("Type your Search", Engine.searchTypes.TEXT)
        val isBackButtonClicked = backButton?.let {
            engine?.click(it, target)
        } == true
        if(!isBackButtonClicked) {
            logToFile("Failed to go back")
            return Pair(false, "Failed to navigate back")
        }

        logToFile("Successfully navigated back")
        return Pair(true, "Successfully navigated back")
    }

    private suspend fun goChats(): Pair<Boolean, String> {
        val target = Pair("MESSAGES", Engine.searchTypes.TEXT)
        val messageMenuButton = engine?.findObjectByClassName("android.view.View")?.get(18)
        val isMessagesMenuButtonClicked = messageMenuButton?.let {
            engine?.click(it, target)
        } == true
        logToFile("failed to go messages")

        if(!isMessagesMenuButtonClicked) {
            logToFile("Failed to click messages button")
            return Pair(false, "Failed to click messages button")
        }

        logToFile("Successfully navigated to messages")
        return Pair(true, "Successfully navigated to messages")
    }

    private suspend fun writeMessageToUser(userName: String): Pair<Boolean, String> {
        val searchUserField = engine?.findObjectByClassName("android.widget.EditText")?.get(0)
        val isSearchUSerFieldFilled = searchUserField?.let {
            engine?.fillTextBox(it, userName, userName, Engine.searchTypes.TEXT)
        } == true

        if(!isSearchUSerFieldFilled) {
            logToFile("Failed to search user to send message")
            return Pair(false, "Failed to search user")
        }
        logToFile("Successfully search user: $userName")

        var target = Pair(userName, Engine.searchTypes.TEXT)
        val firstResult = engine?.findObjectByClassName("android.view.View")?.get(6)
        val isFirstResultSelected = firstResult?.let {
            engine?.click(it, target)
        } == true
        if(!isFirstResultSelected) {
            logToFile("Failed to click first user")
            return Pair(false, "Failed to click first user")
        }

        logToFile("Successfully clicked user")

        val messageBox = engine?.findObjectByClassName("android.widget.EditText")?.get(0)
        val messageText = "Hi there!"
        val isMessageBoxFilled = messageBox?.let {
            engine?.fillTextBox(it, messageText, messageText, Engine.searchTypes.TEXT)
        } == true
        if(!isMessageBoxFilled) {
            logToFile("Failed to write message")
            return Pair(false, "Failed write message")
        }

        logToFile("Successfully wrote message")

        val sendMessageButton = engine?.retrieveNode("Send", Engine.searchTypes.CONTENT_DESC)
        val isSendClicked = sendMessageButton?.let {
            engine?.click(it, target)
        } == true
        if(isSendClicked) {
            logToFile("Failed to send message")
            return Pair(false, "Failed to send message")
        }
        logToFile("Successfully send message")

        target = Pair("MESSAGES", Engine.searchTypes.TEXT)
        val homeNavBarButton = engine?.findObjectByClassName("android.view.View")?.get(19)
        val isHomeButtonClicked = homeNavBarButton?.let {
            engine?.click(it, target)
        } == true

        if(!isHomeButtonClicked) {
            logToFile("Failed to go home page")
            return Pair(false, "Failed to go home page")
        }

        logToFile("Successfully send message to user $userName")
        return Pair(true, "Successfully send message to user $userName")
    }
}
