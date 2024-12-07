package com.ondevice.offdevicetesting

import org.junit.Assert.assertEquals


import org.junit.Test
import kotlin.random.Random

class SimpleDoTest : BaseTestClass("me.jamesfrost.simpledo") {

    private var addedTasks = mutableListOf<String>()
    private var allAddedTasks = mutableListOf<String>()

    @Test
    fun simpleDo() {
        var result = executeTest(::fullApplicationTest, 1000, "SimpleDo full application test")
        assertEquals("Full application test of simple do succeed", result);
    }

    private fun fullApplicationTest(iterations: Int): Pair<Boolean, String> {

        for (i in 0 until iterations) {

            val action = Random.nextInt(0, 2)

            val result: Pair<Boolean, String> = if (action == 0 && addedTasks.size > 0) {
                removeTodo()
            } else {
                addTodo()
            }

            if (!result.first) {
                return Pair(false, result.second)
            }

            logToFile(result.second)

        }

        return Pair(true, "Full application test of simple do succeed")

    }

    private fun addTodo(): Pair<Boolean, String> {


        val todo = getTask()

        var intSucceed = changeText(getResId("toDoName"), todo)
        if (!intSucceed.first) return intSucceed

        intSucceed = clickObjectByRes(getResId("go"))
        if (!intSucceed.first) return intSucceed

        intSucceed = clickObjectByRes(getResId("button"))
        if (!intSucceed.first) return intSucceed

        addedTasks.add(todo)

        return Pair(true, "Successfully added todo: $todo")
    }

    private fun removeTodo(): Pair<Boolean, String> {
        val todo = addedTasks[0]

        val intSucceed = clickObjectByText(todo)
        if (!intSucceed.first) return intSucceed

        addedTasks.remove(todo)

        return Pair(true, "Successfully removed todo: $todo")
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
            "learn",
            "organize",
            "schedule",
            "paint",
            "repair",
            "study",
            "exercise",
            "visit",
            "assemble",
            "discuss",
            "read",
            "review",
            "update",
            "design",
            "implement"
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
            "language",
            "presentation",
            "server",
            "project",
            "office",
            "inventory",
            "furniture",
            "bread",
            "lunch",
            "colleague",
            "program",
            "website",
            "task",
            "meeting",
            "blueprint",
            "module"
        )

        val adverbs = listOf(
            "today",
            "tomorrow",
            "this week",
            "at the weekend",
            "immediately",
            "later",
            "soon",
            "eventually",
            "regularly",
            "occasionally",
            "frequently",
            "rarely",
            "weekly",
            "monthly",
            "yearly"
        )

        var verb = verbs.random()
        var noun = nouns.random()
        var adverb = adverbs.randomOrNull()

        var new_string = "$verb $noun $adverb".trim()

        while (allAddedTasks.contains(new_string)) {
            verb = verbs.random()
            noun = nouns.random()
            adverb = adverbs.randomOrNull()

            new_string = "$verb $noun $adverb".trim()
        }
        allAddedTasks.add(new_string)

        return new_string
    }

}