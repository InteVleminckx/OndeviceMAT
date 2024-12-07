package com.ondevice.offdevicetesting

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.system.measureTimeMillis

@RunWith(AndroidJUnit4::class)
class ExpenseTrackerAppTest : BaseTestClass("com.codewithfk.expensetracker.android") {

    @Test
    fun runExpenseTracker() {

        logToFile("Starting Expense Tracker App Test")
        val result =
            executeTest(::fullExpenseTrackerAppTest, 1, "Expense Tracker App Automation Test")
        logToFile("Expense Tracker Test Result: $result")
        assertEquals("Successfully executed the Expense Tracker app test.", result)

    }

    private fun fullExpenseTrackerAppTest(iterations: Int): Pair<Boolean, String> {
        logToFile("Running full expense tracker app test")
        val executionTime = measureTimeMillis {
            addIncome("5000", 8)
            Thread.sleep(1000)
            addIncome("3000", 3)
            Thread.sleep(1000)
            addExpense("5", 6)
            Thread.sleep(1000)
            addExpense("15", 5)
            Thread.sleep(1000)
            goAnalysis()
            Thread.sleep(1000)
            goHome()
        }
        logToFile("Expense Tracker App test completed in ${executionTime / 1000.0} seconds")
        return Pair(true, "Successfully executed the Expense Tracker app test.")
    }

    private fun clickToAddExpense(): Pair<Boolean, String> {
        logToFile("Attempting to click 'Add Expense' button")
        return try {
            val addExpenseButton =
                device.findObject(UiSelector().description("small floating action button"))
            addExpenseButton.click()
            logToFile("Successfully opened 'Add Expense' view.")
            Pair(true, "Successfully opened 'Add Expense' view.")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to open 'Add Expense' view: ${e.message}")
            Pair(false, "Failed to open 'Add Expense' view: ${e.message}")
        }
    }

    private fun addIncome(amount: String, type: Int): Pair<Boolean, String> {
        logToFile("Starting to add income of $4000")
        return try {
            clickToAddExpense()
            val addIncomeButton = device.findObject(UiSelector().description("Add Income"))
            addIncomeButton.click()

            clickObjectByText("Paypal")
            clickObjectByText("Salary")

            val dollarSymbol = device.findObject(UiSelector().text("$"))
            dollarSymbol.click()

            val amountField = device.findObject(UiSelector().className("android.widget.EditText"))
            amountField.click()
            amountField.setText(amount)

            val targetView =
                device.findObject(UiSelector().className("android.view.View").instance(8))
            targetView.click()

            logToFile("Successfully added income with amount $4000.")
            Pair(true, "Successfully added income with amount $4000.")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to add income: ${e.message}")
            Pair(false, "Failed to add income: ${e.message}")
        }
    }

    private fun addExpense(amount: String, type: Int): Pair<Boolean, String> {
        logToFile("Attempting to add expense of $4")
        return try {
            clickToAddExpense()
            val addExpense = device.findObject(UiSelector().descriptionContains("Add Expense"))
            addExpense.click()

            clickObjectByText("Grocery")
            clickObjectByText("Starbucks")

            val dollarSymbol = device.findObject(UiSelector().text("$"))
            dollarSymbol.click()

            val amountField = device.findObject(UiSelector().className("android.widget.EditText"))
            amountField.click()
            amountField.setText(amount)

            val targetView = device.findObject(UiSelector().className("android.widget.Button"))
            targetView.click()

            logToFile("Successfully added expense with amount $4.")
            Pair(true, "Successfully added expense with amount $4.")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to add expense: ${e.message}")
            Pair(false, "Failed to add expense: ${e.message}")
        }
    }

    private fun goAnalysis(): Pair<Boolean, String> {
        logToFile("Navigating to analysis")
        return try {
            val analysisMenuBarButton =
                device.findObject(UiSelector().className("android.view.View").instance(10))
            analysisMenuBarButton.click()
            logToFile("Successfully navigated to analysis.")
            Pair(true, "Successfully navigated to analysis.")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to navigate to analysis: ${e.message}")
            Pair(false, "Failed to navigate to analysis: ${e.message}")
        }
    }


    private fun goHome(): Pair<Boolean, String> {
        logToFile("Navigating to home")
        return try {
            val homeMenuBarButton =
                device.findObject(UiSelector().className("android.view.View").instance(7))
            homeMenuBarButton.click()
            logToFile("Successfully navigated to home.")
            Pair(true, "Successfully navigated to home.")
        } catch (e: Exception) {
            e.printStackTrace()
            logToFile("Failed to navigate to home: ${e.message}")
            Pair(false, "Failed to navigate to home: ${e.message}")
        }
    }

}
