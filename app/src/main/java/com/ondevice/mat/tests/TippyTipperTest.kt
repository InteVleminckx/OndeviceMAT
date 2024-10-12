package com.ondevice.mat.tests

import android.util.Log
import android.widget.Button
import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import com.ondevice.mat.automation.Engine.searchTypes
import com.ondevice.mat.automation.NodeInfo
import kotlin.system.measureTimeMillis


class TippyTipperTest : Test() {

    override val packageName = "net.mandaria.tippytipper"
    var curScreen: Screen

    enum class ButtonsScreenOne {
        CALC, DEL, CLEAR, ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE
    }

    enum class ButtonsScreenTwo {
        SPLIT, DOWN, UP, TEN, FIFTEEN, TWENTY, BACK1
    }

    enum class ButtonsScreenThree {
        MIN, PLUS, BACK2
    }

    enum class Screen {
        ONE, TWO, THREE
    }

    init {
        appName = "TippyTipper"
        curScreen = Screen.ONE
    }

    override suspend fun runTests() {
        super.runTests()

        executeTest(::fullApplicationTest, 1000, "TippyTipper", false)

    }

    private suspend fun fullApplicationTest(iterations: Int): Pair<Boolean, String> {

        var buttons = getButtons(curScreen)
        var nextScreen: Screen
        var target: Pair<String, searchTypes>?

        for (i in 0 until iterations) {

            val button = buttons.random()

            if (button == ButtonsScreenOne.CALC) {
                nextScreen = Screen.TWO
            } else if (button == ButtonsScreenTwo.SPLIT) {
                nextScreen = Screen.THREE
            } else if (button == ButtonsScreenTwo.BACK1) {
                nextScreen = Screen.ONE
            } else if (button == ButtonsScreenThree.BACK2) {
                nextScreen = Screen.TWO
            } else {
                nextScreen = curScreen
            }

            target = getTarget(nextScreen)
            var interactionSucceed: Boolean
            if (button == ButtonsScreenTwo.BACK1 || button == ButtonsScreenThree.BACK2) {
                interactionSucceed = engine?.pressBack(target) ?: return Pair(false, "Failed to press back")
            } else {
                val resourceId = getResourceId(curScreen, button)
                val buttonNode: NodeInfo = engine?.retrieveNode(resourceId, searchTypes.RESOURCE_ID) ?: return Pair(false, "Failed to retrieve node with resource id: $resourceId")
                interactionSucceed = engine?.click(buttonNode, target) == true
            }

            if (!interactionSucceed) {
                return Pair(false, "Failed to press button: $button")
            }

            curScreen = nextScreen
            buttons = getButtons(curScreen)

            if (i % 99 == 0 && i != 0) {
                Log.v(OUTPUT_TAG, "--- Completed ${i + 1} iterations ---")
            }
            logToFile("Successfully clicked button: $button")

        }

        return Pair(true, "Successfully finished tippy tipper test.")

    }

    private fun getButtons(screen: Screen): List<Any> {

        return when (screen) {
            Screen.ONE -> ButtonsScreenOne.entries
            Screen.TWO -> ButtonsScreenTwo.entries
            Screen.THREE -> ButtonsScreenThree.entries
        }
    }

    private fun getResourceId(screen: Screen, button: Any): String {
        return when (screen) {

            Screen.ONE -> screenOneIds(button)
            Screen.TWO -> screenTwoIds(button)
            Screen.THREE -> screenThreeIds(button)
        }
    }

    private fun screenOneIds(button: Any): String {
        return when (button) {
            ButtonsScreenOne.CALC -> "btn_ok"
            ButtonsScreenOne.DEL -> "btn_delete"
            ButtonsScreenOne.CLEAR -> "btn_clear"
            ButtonsScreenOne.ZERO -> "btn_zero"
            ButtonsScreenOne.ONE -> "btn_one"
            ButtonsScreenOne.TWO -> "btn_two"
            ButtonsScreenOne.THREE -> "btn_three"
            ButtonsScreenOne.FOUR -> "btn_four"
            ButtonsScreenOne.FIVE -> "btn_five"
            ButtonsScreenOne.SIX -> "btn_six"
            ButtonsScreenOne.SEVEN -> "btn_seven"
            ButtonsScreenOne.EIGHT -> "btn_eight"
            ButtonsScreenOne.NINE -> "btn_nine"
            else -> {""}
        }
    }

    private fun screenTwoIds(button: Any): String {
        return when (button) {
            ButtonsScreenTwo.SPLIT -> "btn_SplitBill"
            ButtonsScreenTwo.DOWN -> "btn_round_down"
            ButtonsScreenTwo.UP -> "btn_round_up"
            ButtonsScreenTwo.TEN -> "btn_TipAmount1"
            ButtonsScreenTwo.FIFTEEN -> "btn_TipAmount2"
            ButtonsScreenTwo.TWENTY -> "btn_TipAmount3"
            ButtonsScreenTwo.BACK1 -> "back_1"
            else -> {""}
        }
    }

    private fun screenThreeIds(button: Any): String {
        return when (button) {
            ButtonsScreenThree.MIN -> "btn_remove_person"
            ButtonsScreenThree.PLUS -> "btn_add_person"
            ButtonsScreenThree.BACK2 -> "back_2"
            else -> {""}
        }
    }

    private fun getTarget(screen: Screen): Pair<String, searchTypes> {
        return when (screen) {
            Screen.ONE -> Pair(getResourceId(Screen.ONE, ButtonsScreenOne.ONE), searchTypes.RESOURCE_ID)
            Screen.TWO -> Pair(getResourceId(Screen.TWO, ButtonsScreenTwo.SPLIT), searchTypes.RESOURCE_ID)
            Screen.THREE -> Pair(getResourceId(Screen.THREE, ButtonsScreenThree.MIN), searchTypes.RESOURCE_ID)
        }
    }

}