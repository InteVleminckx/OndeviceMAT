package com.ondevice.mat.tests;

import android.util.Log
import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class SimpleDoTest : Test() {

    override val packageName = "me.jamesfrost.simpledo"
    private var addedTasks = mutableListOf<String>()

    override suspend fun runTests() {
        super.runTests()

        val numberOfInteractions = 10
        if (engine != null) {
            Log.v(OUTPUT_TAG, "--- Starting Full Application Test ---")
            val executionTime = measureTimeMillis {
                fullApplicationTest(numberOfInteractions)
            }
            Log.v(OUTPUT_TAG, "--- Full Application Test Finished $numberOfInteractions interactions in ${executionTime / 1000.0} seconds---")
        }

    }

    private fun getTask(): String {
        val verbs = listOf("write", "fix", "call", "clean", "order", "water", "buy", "cook", "meet", "learn")
        val nouns = listOf("report", "computer", "client", "room", "groceries", "plants", "milk", "dinner", "friend", "language")
        val adverbs = listOf("today", "tomorrow", "this week", "at the weekend")

        val verb = verbs.random()
        val noun = nouns.random()
        val adverb = adverbs.randomOrNull()

        return "$verb $noun $adverb".trim()
    }

    private suspend fun addTodo(): Boolean {

        // Enter text in the text box
        val todo = getTask()
        var resourceId = "toDoName"
        val textBox = engine?.retrieveNode(resourceId, Engine.searchTypes.RESOURCE_ID, allNodes = true) ?: return false
        val textEntered = engine?.fillTextBox(textBox, todo, resourceId, Engine.searchTypes.RESOURCE_ID) == true
        if (!textEntered) return false

        // Press Go button
        resourceId = "go"
        val goButton = engine?.retrieveNode(resourceId, Engine.searchTypes.RESOURCE_ID) ?: return false
        var target = Pair("button", Engine.searchTypes.RESOURCE_ID)
        val pressedGo = engine?.click(goButton, target) ?: false
        if (!pressedGo) return false

        // Press Done button
        resourceId = "button"
        val doneButton = engine?.retrieveNode(resourceId, Engine.searchTypes.RESOURCE_ID) ?: return false
        target = Pair("go", Engine.searchTypes.RESOURCE_ID)
        val pressedDone = engine?.click(doneButton, target) ?: false
        if (!pressedDone) return false

        addedTasks.add(todo)
        return true

    }

    private suspend fun removeTodo(): Boolean {
        val todo = addedTasks.random()

        val todoNode = engine?.retrieveNode(todo, Engine.searchTypes.TEXT) ?: return false
        val target = Pair("go", Engine.searchTypes.RESOURCE_ID)
        val deletedTodo = engine?.removeClick(todoNode, target) ?: false
        if (!deletedTodo) return false

        addedTasks.remove(todo)
        return true
    }


    private suspend fun fullApplicationTest(numberOfInteractions: Int) {

        for (i in 0..numberOfInteractions) {

            val action = Random.nextInt(0, 2)

            val success: Boolean = if (action == 0 && addedTasks.size > 0) {
                removeTodo()
            } else {
                addTodo()
            }

            if (!success) {
                Log.v(OUTPUT_TAG, "--- Full Application Test Failed after $i interactions ---")
                return
            }
        }
    }

}
