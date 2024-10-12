package com.ondevice.offdevicetesting

import android.util.Log
import org.junit.Test

class TippyTipperTest : BaseTestClass("net.mandaria.tippytipper") {

    var nextScreen: Screen = Screen.ONE;

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

    @Test
    fun tippyTipper() {
        for (i in 0 until 21) {
            executeTest(::fullApplicationTest, 1000, "Tippy Tipper test")
            createNewFile()
        }
    }

    private fun fullApplicationTest(iterations: Int): Pair<Boolean, String> {

        for (i in 0 until iterations) {

            val curScreen = nextScreen

            val button = getButtons(curScreen).random()

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

            var interactionSucceed: Pair<Boolean, String>

            if (button == ButtonsScreenTwo.BACK1 || button == ButtonsScreenThree.BACK2) {
                device.pressBack()
                interactionSucceed = Pair(true, "Press back succeed.")
            } else {
//                Log.v("testTag", getResId(getResourceId(curScreen, button)))
                interactionSucceed = clickObjectByRes(getResId(getResourceId(curScreen, button)))
            }

            if (!interactionSucceed.first) return interactionSucceed

            if (!waitUntilNextScreen(nextScreen)) return Pair(false, "Didn't end up on the correct screen, expect screen: ${nextScreen.name}")

        }

        return Pair(true, "Successfully finished tippy tipper test.")
    }

    private fun waitUntilNextScreen(nextScreen: Screen): Boolean {
        return when (nextScreen) {
            Screen.ONE -> screenContainsId(getResId("btn_ok"), timeout)
            Screen.TWO -> screenContainsId(getResId("btn_round_down"), timeout)
            Screen.THREE -> screenContainsId(getResId("btn_remove_person"), timeout)
        }
    }


    private fun getScreen(): Screen {

        var screen: Screen? = null

        while (screen == null) {
            if (screenContainsId(getResId("btn_ok"))) {
                screen = Screen.ONE
            } else if (screenContainsId(getResId("btn_round_down"))) {
                screen = Screen.TWO
            } else if (screenContainsId(getResId("btn_remove_person"))) {
                screen = Screen.THREE
            }
        }

        return screen

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
            else -> {
                ""
            }
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
            else -> {
                ""
            }
        }
    }

    private fun screenThreeIds(button: Any): String {
        return when (button) {
            ButtonsScreenThree.MIN -> "btn_remove_person"
            ButtonsScreenThree.PLUS -> "btn_add_person"
            ButtonsScreenThree.BACK2 -> "back_2"
            else -> {
                ""
            }
        }
    }

}