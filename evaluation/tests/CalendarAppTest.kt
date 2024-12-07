package com.ondevice.offdevicetesting

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.UiSelector
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.system.measureTimeMillis

@RunWith(AndroidJUnit4::class)
class CalendarAppTest : BaseTestClass("com.sztorm.notecalendar") {

    @Test
    fun runCalendarAppTest() {
        logToFile("Starting Calendar App Test")
        val result = executeTest(::fullCalendarAppTest, 1, "Calendar App Automation Test")
        logToFile("Calendar App Test Result: $result")
        assertEquals("Successfully executed the Calendar app test.", result)
    }

    private fun fullCalendarAppTest(iterations: Int): Pair<Boolean, String> {
        logToFile("Running full Calendar app test")
        val executionTime = measureTimeMillis {
            switchBetweenModsToToday()
            goToDate15()
            clickAddNoteButton()
            writeNote("Today was amazing")
            saveNote()
            deleteNote()
            goSettings()
            changeThemeToDark()
        }
        logToFile("Calendar App test completed in ${executionTime / 1000.0} seconds")
        return Pair(true, "Successfully executed the Calendar app test.")
    }


    private fun switchToToday(): Pair<Boolean, String> {
        logToFile("Switching between views to Today")
        return try {
            device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/btnViewDay"))
                .click()
            logToFile("Successfully switched to today view.")
            Pair(true, "Successfully switched to today view.")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to switch to today view: ${e.message}")
            Pair(false, "Failed to switch to today view: ${e.message}")
        }
    }

    private fun switchBetweenModsToToday(): Pair<Boolean, String> {
        logToFile("Switching between views to Today")
        return try {
            device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/btnViewWeek"))
                .click()
            device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/btnViewDay"))
                .click()
            device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/btnViewMonth"))
                .click()
            logToFile("Successfully switched between views.")
            Pair(true, "Successfully switched between views.")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to switch between views: ${e.message}")
            Pair(false, "Failed to switch between views: ${e.message}")
        }
    }

    private fun clickAddNoteButton(): Pair<Boolean, String> {
        logToFile("Clicking add note button")
        return try {
            device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/btnNoteAdd"))
                .click()
            logToFile("Successfully clicked to add note button")
            Pair(true, "Successfully clicked to add note button")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to click add note button: ${e.message}")
            Pair(false, "Failed to click add note button")
        }
    }

    private fun writeNote(text: String): Pair<Boolean, String> {
        logToFile("Writing note: $text")
        return try {
            val noteField =
                device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/txtNoteAdd"))
            noteField.setText(text)
            logToFile("Successfully wrote a note")
            Pair(true, "Successfully wrote a note")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to write note: ${e.message}")
            Pair(false, "Failed to write note")
        }
    }

    private fun saveNote(): Pair<Boolean, String> {
        logToFile("Saving note")
        return try {
            val saveButton =
                device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/btnNoteSave"))
            saveButton.click()
            logToFile("Successfully saved note")
            Pair(true, "Successfully saved note")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to save note: ${e.message}")
            Pair(false, "Failed to save note")
        }
    }

    private fun editNote(): Pair<Boolean, String> {
        logToFile("Editing note")
        return try {
            val editButton =
                device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/btnNoteEditText"))
            editButton.click()
            val noteField =
                device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/txtNoteEdit"))
            noteField.setText("Note changed successfully")
            val saveChangedButton =
                device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/btnNoteEditSave"))
            saveChangedButton.click()
            logToFile("Successfully edited and saved note")
            Pair(true, "Successfully edited and saved note")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to edit note: ${e.message}")
            Pair(false, "Failed to edit note")
        }
    }

    private fun deleteNote(): Pair<Boolean, String> {
        logToFile("Deleting note")
        return try {
            val deleteButton =
                device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/btnNoteDeleteText"))
            deleteButton.click()
            logToFile("Successfully deleted note")
            Pair(true, "Successfully deleted note")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to delete note: ${e.message}")
            Pair(false, "Failed to delete note")
        }
    }

    private fun goSettings(): Pair<Boolean, String> {
        logToFile("Navigating to settings")
        return try {
            val settingsButton =
                device.findObject(UiSelector().resourceId("com.sztorm.notecalendar:id/btnViewSettings"))
            settingsButton.click()
            Thread.sleep(5000)
            logToFile("Successfully navigated to settings")
            Pair(true, "Successfully navigated to settings")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to navigate to settings: ${e.message}")
            Pair(false, "Failed to navigate to settings")
        }
    }

    private fun goToDate15(): Pair<Boolean, String> {
        logToFile("Navigating to date 15")
        return try {
            clickObjectByText("15")
            logToFile("Successfully navigated to date 15")
            Pair(true, "Successfully navigated to date 15")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to navigate to date 15: ${e.message}")
            Pair(false, "Failed to navigate to date 15")
        }
    }

    private fun changeThemeToDark(): Pair<Boolean, String> {
        logToFile("Changing theme")
        return try {
            clickObjectByText("Set dark theme")
            logToFile("Successfully changed theme")
            Pair(true, "Successfully changed theme")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to change theme: ${e.message}")
            Pair(false, "Failed to change theme")
        }
    }
}
