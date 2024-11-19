package com.ondevice.offdevicetesting

import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import kotlinx.coroutines.delay
import kotlin.math.log

class ExpenseTrackerTest : Test() {

    override val packageName = "com.codewithfk.expensetracker.android"
    init {
        appName = "ExpenseTracker"
    }

    override suspend fun runTests() {
        super.runTests()
        executeTest(::fullExpenseTrackerAppTest, 5, "Expense Tracker App Automation Test", false)
    }

    private suspend fun fullExpenseTrackerAppTest(iterations: Int): Pair<Boolean, String> {

        clickToIncome()
        fillIncomeDetails("5000", 8)
        Thread.sleep(1000)

        clickToIncome()
        fillIncomeDetails("5000", 3)
        Thread.sleep(1000)

        clickToAddCost()
        fillCostDetails("5", 6)
        Thread.sleep(1000)

        clickToAddCost()
        fillCostDetails("15", 5)
        Thread.sleep(1000)

        goAnalysis()

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
        clickToAddExpense()
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

    private suspend fun fillIncomeDetails(amount: String, type: Int): Pair<Boolean, String> {
        logToFile("Creating income")

        val incomeTypeButton = engine?.findObjectByClassName("android.widget.Spinner")?.get(0)
        var target = Pair("Paypal", Engine.searchTypes.TEXT)
        val clickedIncomeTypeButton = incomeTypeButton?.let { engine?.click(it, target) } == true

        if(!clickedIncomeTypeButton) {
            logToFile("Failed to click select income type")
            return Pair(false, "Could not select income type")
        }

        target = Pair("$", Engine.searchTypes.TEXT)
        val newIncomeTypeButton = engine?.findObjectByClassName("android.view.View")?.get(7)
        val clickedNewIncomeTypeButton = newIncomeTypeButton?.let {
            engine?.click(it, target)
        } == true

        if(!clickedNewIncomeTypeButton) {
            logToFile("Could not clicked to salary")
            return Pair(false, "Could not clicked to salary")
        }

        val amountField = engine?.retrieveNode("$", Engine.searchTypes.TEXT)  ?: return Pair(false, "Failed to retrieve amount fill text  field ")
        val filledAmountField =  engine?.fillTextBox(amountField, amount, "$$amount", Engine.searchTypes.TEXT) == true

        if (!filledAmountField) {
            logToFile("Could not fill the amount field")
            return Pair(false, "Could not fill the amount field")
        }

        target = Pair("Add Income", Engine.searchTypes.TEXT)
        val addIncomeButton = engine?.findObjectByClassName("android.widget.Button")?.get(0)
        val isAddIncomeButtonClicked = addIncomeButton?.let {
            engine?.click(it, target)
        } == true

        if(!isAddIncomeButtonClicked) {
            logToFile("Failed to save income")
            return Pair(false, "Could not save income")
        }

        logToFile("Successfully selected income type")
        return Pair(true, "Successfully selected income type")
    }

    private suspend fun clickToAddCost(): Pair<Boolean, String> {
        clickToAddExpense()
        val costButton = engine?.retrieveNode("Add Expense", Engine.searchTypes.CONTENT_DESC)
        val target = Pair("Paypal", Engine.searchTypes.TEXT)
        val clickedCostButton = costButton?.let {
            engine?.click(it, target)
        } == true

        if(!clickedCostButton) {
            logToFile("Failed to click add cost button")
            return Pair(false, "Could not click to add cost button")
        }

        logToFile("Successfully clicked to add cost")
        return Pair(true, "Successfully clicked to add cost")
    }

    private suspend fun fillCostDetails(amount: String, type: Int): Pair<Boolean, String> {
        val costTypeButton = engine?.findObjectByClassName("android.widget.Spinner")?.get(0)
        var target = Pair("Grocery", Engine.searchTypes.TEXT)
        val clickedCostTypeButton = costTypeButton?.let { engine?.click(it, target) } == true

        if(!clickedCostTypeButton) {
            logToFile("Failed to click select cost type")
            return Pair(false, "Could not select cost type")
        }

        target = Pair("$", Engine.searchTypes.TEXT)
        val newIncomeTypeButton = engine?.findObjectByClassName("android.view.View")?.get(type)
        val clickedNewIncomeTypeButton = newIncomeTypeButton?.let {
            engine?.click(it, target)
        } == true

        if(!clickedNewIncomeTypeButton) {
            logToFile("Could not clicked to salary")
            return Pair(false, "Could not clicked to salary")
        }

        val amountField = engine?.retrieveNode("$", Engine.searchTypes.TEXT)  ?: return Pair(false, "Failed to retrieve amount fill text  field ")
        val filledAmountField =  engine?.fillTextBox(amountField, amount, "$$amount", Engine.searchTypes.TEXT) == true

        if (!filledAmountField) {
            logToFile("Could not fill the amount field")
            return Pair(false, "Could not fill the amount field")
        }

        target = Pair("Add Expense", Engine.searchTypes.TEXT)
        val addIncomeButton = engine?.findObjectByClassName("android.widget.Button")?.get(0)
        val isExpenseButtonClicked = addIncomeButton?.let {
            engine?.click(it, target)
        } == true

        if(!isExpenseButtonClicked) {
            logToFile("Failed to save income")
            return Pair(false, "Could not save income")
        }

        logToFile("Successfully selected income type")
        return Pair(true, "Successfully selected income type")
    }

    private suspend fun goAnalysis(): Pair<Boolean, String> {
        val analysisMenuBarButton = engine?.findObjectByClassName("android.view.View")?.get(10)
        val target = Pair("Statistics", Engine.searchTypes.TEXT)
        val isAnalysisClicked = analysisMenuBarButton?.let {
            engine?.click(it, target)
        } == true
        if(!isAnalysisClicked) {
            logToFile("Failed to click on analysis")
            return Pair(false, "Failed to click on analysis")
        }

        return Pair(true, "Successfully navigated to Analysis")
    }

}