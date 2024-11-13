package com.ondevice.offdevicetesting

import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine

class ExpenseTrackerTest : Test() {

    override val packageName = "com.codewithfk.expensetracker.android"

    init {
        appName = "ExpenseTracker"
    }

    override suspend fun runTests() {
        super.runTests()
        executeTest(::fullExpenseTrackerAppTest, 1, "Expense Tracker App Automation Test", false)
    }

    private suspend fun fullExpenseTrackerAppTest(iterations: Int): Pair<Boolean, String> {
        clickToAddExpense()
        return Pair(true, "Successfully completed the Expense Tracker app test.")
    }

    private suspend fun clickToAddExpense(): Pair<Boolean, String> {
        logToFile("Clicking 'Add Expense' button")

        val addExpenseButtonResourceId = "small floating action button"
        val addExpenseButton = engine?.retrieveNode(addExpenseButtonResourceId, Engine.searchTypes.CONTENT_DESC, allNodes = true)
            ?: return Pair(false, "Failed to retrieve 'Add Expense' button with resource ID: $addExpenseButtonResourceId")

        val target = Pair("Add Expense", Engine.searchTypes.CONTENT_DESC)
        val clickedAddExpenseButton = engine?.click(addExpenseButton, target) == true
        if (!clickedAddExpenseButton) {
            logToFile("Failed to click 'Add Expense' button")
            return Pair(false, "Couldn't click the 'Add Expense' button with resource ID: $addExpenseButtonResourceId")
        }

        logToFile("Successfully opened 'Add Expense' view.")
        return Pair(true, "Successfully opened 'Add Expense' view.")
    }
}