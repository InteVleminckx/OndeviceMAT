package com.ondevice.offdevicetesting

import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import kotlinx.coroutines.delay

class CalendarTest : Test() {

    override val packageName = "com.sztorm.notecalendar"

    init {
        appName = "CalendarApp"
    }

    override suspend fun runTests() {
        super.runTests()
        executeTest(::fullCalendarAppTest, 1, "Calendar App Automation Test", false)
    }

    private suspend fun fullCalendarAppTest(iterations: Int): Pair<Boolean, String> {
        for (i in 0 until iterations) {
            logToFile("Iteration ${i + 1}")

            if (!switchBetweenViewsToToday()) return Pair(false, "Failed to switch views.")
            if (!navigateToDate("15")) return Pair(false, "Failed to navigate to date 15.")
            if (!clickAddNoteButton()) return Pair(false, "Failed to click add note button.")
            if (!writeNoteText("Today was amazing")) return Pair(false, "Failed to write note.")
            if (!saveNote()) return Pair(false, "Failed to save note.")
            if (!deleteNote()) return Pair(false, "Failed to delete note.")
            if (!goToSettings()) return Pair(false, "Failed to navigate to settings.")
            toggleNotifications()
        }
        return Pair(true, "Successfully completed the Calendar app test.")
    }

    private suspend fun switchBetweenViewsToToday(): Boolean {
        logToFile("Switching between views to Today")
        return try {
            val weekNode = engine?.retrieveNode(
                "com.sztorm.notecalendar:id/btnViewWeek",
                Engine.searchTypes.RESOURCE_ID
            )
            val dayNode = engine?.retrieveNode(
                "com.sztorm.notecalendar:id/btnViewDay",
                Engine.searchTypes.RESOURCE_ID
            )
            val monthNode = engine?.retrieveNode(
                "com.sztorm.notecalendar:id/btnViewMonth",
                Engine.searchTypes.RESOURCE_ID
            )

            weekNode?.let {
                engine?.click(
                    it,
                    Pair("com.sztorm.notecalendar:id/btnViewDay", Engine.searchTypes.RESOURCE_ID)
                )
            } == true &&
                    dayNode?.let {
                        engine?.click(
                            it,
                            Pair(
                                "com.sztorm.notecalendar:id/btnViewMonth",
                                Engine.searchTypes.RESOURCE_ID
                            )
                        )
                    } == true &&
                    monthNode?.let {
                        engine?.click(
                            it,
                            Pair(
                                "com.sztorm.notecalendar:id/btnViewWeek",
                                Engine.searchTypes.RESOURCE_ID
                            )
                        )
                    } == true
        } catch (e: Exception) {
            logToFile("Failed to switch views: ${e.message}")
            false
        }
    }

    private suspend fun navigateToDate(date: String): Boolean {
        logToFile("Navigating to date $date")
        return try {
            engine?.retrieveNode(date, Engine.searchTypes.TEXT)
                ?.let { engine?.click(it, Pair(date, Engine.searchTypes.TEXT)) } ?: false
        } catch (e: Exception) {
            logToFile("Failed to navigate to date $date: ${e.message}")
            false
        }
    }

    private suspend fun clickAddNoteButton(): Boolean {
        logToFile("Clicking add note button")
        return try {
            val node = engine?.retrieveNode(
                "com.sztorm.notecalendar:id/btnNoteAdd",
                Engine.searchTypes.RESOURCE_ID
            )
            node?.let {
                engine?.click(
                    it,
                    Pair("com.sztorm.notecalendar:id/txtNoteAdd", Engine.searchTypes.RESOURCE_ID)
                )
            } ?: false
        } catch (e: Exception) {
            logToFile("Failed to click add note button: ${e.message}")
            false
        }
    }

    private suspend fun writeNoteText(text: String): Boolean {
        logToFile("Writing note: $text")
        return try {
            val node = engine?.retrieveNode(
                "com.sztorm.notecalendar:id/txtNoteAdd",
                Engine.searchTypes.RESOURCE_ID
            )
            node?.let {
                engine?.fillTextBox(
                    it,
                    text,
                    "com.sztorm.notecalendar:id/txtNoteAdd",
                    Engine.searchTypes.RESOURCE_ID
                )
            } ?: false
        } catch (e: Exception) {
            logToFile("Failed to write note text: ${e.message}")
            false
        }
    }

    private suspend fun saveNote(): Boolean {
        logToFile("Saving note")
        return try {
            val node = engine?.retrieveNode(
                "com.sztorm.notecalendar:id/btnNoteSave",
                Engine.searchTypes.RESOURCE_ID
            )
            node?.let {
                engine?.click(
                    it,
                    Pair(
                        "com.sztorm.notecalendar:id/btnNoteEditText",
                        Engine.searchTypes.RESOURCE_ID
                    )
                )
            } ?: false
        } catch (e: Exception) {
            logToFile("Failed to save note: ${e.message}")
            false
        }
    }

    private suspend fun deleteNote(): Boolean {
        logToFile("Deleting note")
        return try {
            val node = engine?.retrieveNode(
                "com.sztorm.notecalendar:id/btnNoteDeleteText",
                Engine.searchTypes.RESOURCE_ID
            )
            node?.let {
                engine?.click(
                    it,
                    Pair("com.sztorm.notecalendar:id/btnNoteAdd", Engine.searchTypes.RESOURCE_ID)
                )
            } ?: false
        } catch (e: Exception) {
            logToFile("Failed to delete note: ${e.message}")
            false
        }
    }

    private suspend fun goToSettings(): Boolean {
        logToFile("Navigating to settings")
        return try {
            val node = engine?.retrieveNode(
                "com.sztorm.notecalendar:id/btnViewSettings",
                Engine.searchTypes.RESOURCE_ID
            )
            node?.let {
                engine?.click(
                    it,
                    Pair("Settings clicked", Engine.searchTypes.RESOURCE_ID)
                )
            } ?: false
        } catch (e: Exception) {
            logToFile("Failed to navigate to settings: ${e.message}")
            false
        }
    }


    private suspend fun toggleNotifications(): Pair<Boolean, String> {
        logToFile("Starting to toggle notifications on/off")

        // Resource ID for the switch widget
        val switchResourceId = "switchWidget"

        // Attempt to retrieve the switch element by resource ID
        val switchNode = engine?.retrieveNode(switchResourceId, Engine.searchTypes.RESOURCE_ID)
            ?: return Pair(false, "Failed to retrieve switch with resource ID: $switchResourceId")

        // Attempt to toggle the switch by clicking on it
        val switchClicked = engine?.click(
            switchNode,
            Pair(switchResourceId, Engine.searchTypes.RESOURCE_ID)
        ) == true
        if (!switchClicked) {
            logToFile("Failed to click the switch for notifications")
            return Pair(false, "Couldn't click the switch with resource ID: $switchResourceId")
        }

        delay(500) // Brief delay to allow the UI to reflect the toggle state

        logToFile("Successfully toggled notifications")
        return Pair(true, "Notifications toggled successfully")
    }
}

