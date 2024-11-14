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
        clickToIncome()
        fillIncomeDetails()
        return Pair(true, "Successfully completed the Expense Tracker app test.")
    }

    private suspend fun clickToAddExpense(): Pair<Boolean, String> {
        logToFile("Clicking 'Add Expense' button")

        val addExpenseButtonContentDesc= "small floating action button"
        val addExpenseButton = engine?.retrieveNode(addExpenseButtonContentDesc, Engine.searchTypes.CONTENT_DESC, allNodes = true)
            ?: return Pair(false, "Failed to retrieve 'Add Expense' button with resource ID: $addExpenseButtonContentDesc")

        val target = Pair("Good Afternoon", Engine.searchTypes.TEXT)
        val clickedAddExpenseButton = engine?.click(addExpenseButton, target) == true
        if (!clickedAddExpenseButton) {
            logToFile("Failed to click 'Add Expense' button")
            return Pair(false, "Couldn't click the 'Add Expense' button with resource ID: $addExpenseButtonContentDesc")
        }

        logToFile("Successfully opened 'Add Expense' view.")
        return Pair(true, "Successfully opened 'Add Expense' view.")
    }

    private suspend fun clickToIncome(): Pair<Boolean, String> {
        logToFile("Clicking to add income button")

        val addIncomeButtonContentDesc = "Add Income"
        val addIncomeButton = engine?.retrieveNode(addIncomeButtonContentDesc, Engine.searchTypes.CONTENT_DESC, allNodes = true)
            ?: return Pair(false, "Failed to retrieve 'Add Income' button with content description $addIncomeButtonContentDesc")
        val target = Pair("Add Income", Engine.searchTypes.TEXT)
        val clickedAddIncomeButton = engine?.click(addIncomeButton, target) == true
        if(!clickedAddIncomeButton) {
            logToFile("Failed to click add income")
            return Pair(false, "Could not click to Add Income button with content description $addIncomeButtonContentDesc")
        }
        logToFile("Successfully clicked 'Add Income' button")
        return Pair(true, "Successfully clicked 'Add Income' button")
    }

    private suspend fun fillIncomeDetails(): Pair<Boolean, String> {
        logToFile("Creating income")

        val incomeTypeButton = engine?.findObjectByClassName("android.widget.Spinner")?.get(0)
        var target = Pair("Paypal", Engine.searchTypes.TEXT)
        val clickedIncomeTypeButton = incomeTypeButton?.let { engine?.click(it, target) } == true

        if(!clickedIncomeTypeButton) {
            logToFile("Failed to click select income type")
            return Pair(false, "Could not select income type")
        }

        target = Pair("$", Engine.searchTypes.TEXT)
        val newIncomeTypeButton = engine?.findObjectByClassName("android.view.View")?.get(4)
        val clickedNewIncomeTypeButton = newIncomeTypeButton?.let {
            engine?.click(it, target)
        } == true

        if(!clickedNewIncomeTypeButton) {
            logToFile("Could not clicked to salary")
            return Pair(false, "Could not clicked to salary")
        }

        val amountField = engine?.retrieveNode("$", Engine.searchTypes.TEXT)  ?: return Pair(false, "Failed to retrieve amount fill text  field ")
        val filledAmountField =  engine?.fillTextBox(amountField, "4000", "$", Engine.searchTypes.TEXT) == true
        logToFile(amountField.toString())
        logToFile(amountField.nodeText())
        if (!filledAmountField) {
            logToFile("Could not fill the amount field")
            return Pair(false, "Could not fill the amount field")
        }

        logToFile("Successfully selected income type")
        return Pair(true, "")

//        val selectDate = engine?.findObjectByClassName("android.widget.EditText")?.get(1)
//        target = Pair("Selected date", Engine.searchTypes.TEXT)
//        val clickedDate = selectDate?.let {
//            engine?.click(it, target)
//        } == true
//
//        if(!clickedDate) {
//            logToFile("Could not click to select date")
//            return Pair(false, "Could not click to select date")
//        }
//
//        val newDateText = "Friday, November 15, 2024"
//        val newDateComponent = engine?.retrieveNode(newDateText, Engine.searchTypes.TEXT)
//        val newDateComponentSelected = newDateComponent?.let {
//            engine?.click(it, target)
//        } == true
//
//        if(!newDateComponentSelected) {
//            logToFile("Could not click to 15 November Date")
//            return Pair(false, "Failed to select date.")
//        }
//
//        target = Pair("Add Income", Engine.searchTypes.TEXT)
//        val confirmButton = engine?.findObjectByClassName("android.widget.Button")?.get(35)
//        val clickedConfirmButton = confirmButton?.let {
//            engine?.click(it, target)
//        } == true
//
//        if(!clickedConfirmButton) {
//            logToFile("Failed to confirm the date")
//            return Pair(false, "Could not click to confirm")
//        }
//
//        logToFile("Successfully selected income type")
//        return Pair(true, "")
    }


}