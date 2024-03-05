package com.ondevice.mat.tests

import android.util.Log
import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import com.ondevice.mat.automation.Interactor
import com.ondevice.mat.automation.NodeInfo

class TicTacToeTest : Test() {

    override val packageName = "ug.lecode.tictactoe"

    override suspend fun runTests() {
        super.runTests()

        if (engine != null) {
            testTicTacTwo()
        }

    }

    private suspend fun testTicTacToe() {

        val interactions: List<String> = listOf("one", "three", "five")


        for (interaction in interactions) {
            var interactionNode: NodeInfo = engine?.retrieveNode(interaction, Engine.searchTypes.RESOURCE_ID) ?: return

            if (interactionNode.nodeText() != "") {
                continue
            }

            val clickSucceeded =
                engine?.click(interactionNode, Pair(interaction, Engine.searchTypes.RESOURCE_ID)) == true

            if (!clickSucceeded) {
                Log.v("DebugTag", "Click failed")

            } else {
                Log.v("DebugTag", "Click succeeded")
                interactionNode = engine?.retrieveNode(interaction, Engine.searchTypes.RESOURCE_ID) ?: return

                Log.v("DebugTag", "Node is marked: ${interactionNode.nodeText() != ""}")
            }
        }
    }

    private suspend fun testTicTacTwo() {

        engine?.pressHome()

        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)
        engine?.swipe(Interactor.SwipeDirection.UP)

    }

}