package com.ondevice.mat.tests;

import android.util.Log
import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class SimpleDoTest : Test() {

    override val packageName = "me.jamesfrost.simpledo"
    private var addedTasks = mutableListOf<String>()
    init {
        appName = "SimpleDo"
    }


    override suspend fun runTests() {
        super.runTests()

        executeTest(::fullApplicationTest, 1000, "SimpleDo full application test")


    }

    private fun getTask(): String {
        val verbs = listOf(
            "write",
            "fix",
            "call",
            "clean",
            "order",
            "water",
            "buy",
            "cook",
            "meet",
            "learn"
        )
        val nouns = listOf(
            "report",
            "computer",
            "client",
            "room",
            "groceries",
            "plants",
            "milk",
            "dinner",
            "friend",
            "language"
        )
        val adverbs = listOf("today", "tomorrow", "this week", "at the weekend")

        val verb = verbs.random()
        val noun = nouns.random()
        val adverb = adverbs.randomOrNull()

        return "$verb $noun $adverb".trim()
    }

    private suspend fun addTodo(): Pair<Boolean, String> {

        // Enter text in the text box
        val todo = getTask()
        var resourceId = "toDoName"
        val textBox =
            engine?.retrieveNode(resourceId, Engine.searchTypes.RESOURCE_ID, allNodes = true)
                ?: return Pair(false, "Failed to retrieve node with resource id: $resourceId")
        val textEntered =
            engine?.fillTextBox(textBox, todo, resourceId, Engine.searchTypes.RESOURCE_ID) == true
        if (!textEntered) return Pair(true, "Couldn't click object with resource id: $resourceId")

        // Press Go button
        resourceId = "go"
        val goButton =
            engine?.retrieveNode(resourceId, Engine.searchTypes.RESOURCE_ID) ?: return Pair(
                false,
                "Failed to retrieve node with resource id: $resourceId"
            )
        var target = Pair("button", Engine.searchTypes.RESOURCE_ID)
        val pressedGo = engine?.click(goButton, target) ?: false
        if (!pressedGo) {
            return Pair(false, "Couldn't click object with resource id: $resourceId")
        }

        // Press Done button
        resourceId = "button"
        val doneButton =
            engine?.retrieveNode(resourceId, Engine.searchTypes.RESOURCE_ID) ?: return Pair(
                false,
                "Failed to retrieve node with resource id: $resourceId"
            )
        target = Pair("go", Engine.searchTypes.RESOURCE_ID)
        val pressedDone = engine?.click(doneButton, target) ?: false
        if (!pressedDone) return Pair(false, "Couldn't click object with resource id: $resourceId")

        addedTasks.add(todo)
        return Pair(true, "Successfully added todo: $todo")

    }

    private suspend fun removeTodo(): Pair<Boolean, String> {
        val todo = addedTasks.random()

        val todoNode = engine?.retrieveNode(todo, Engine.searchTypes.TEXT) ?: return Pair(
            false,
            "Failed to retrieve node $todo"
        )
        val target = Pair("go", Engine.searchTypes.RESOURCE_ID)
        val deletedTodo = engine?.removeClick(todoNode, target) ?: return Pair(
            false,
            "Failed to remove todo: $todo"
        )
        if (!deletedTodo) return Pair(false, "Failed to remove todo: $todo")

        addedTasks.remove(todo)
        return Pair(true, "Successfully removed todo: $todo")
    }


    private suspend fun fullApplicationTest(numberOfInteractions: Int): Pair<Boolean, String> {

        for (i in 0..numberOfInteractions) {

            val action = Random.nextInt(0, 2)

            val success: Pair<Boolean, String> = if (action == 0 && addedTasks.size > 0) {
                removeTodo()
            } else {
                addTodo()
            }

            if (!success.first) {
                Log.v(OUTPUT_TAG, "--- Full Application Test Failed after $i interactions ---")
                return Pair(false, success.second)
            }

            logToFile(success.second)
        }
        return Pair(true, "Full application of simple do succeed.")
    }

}
