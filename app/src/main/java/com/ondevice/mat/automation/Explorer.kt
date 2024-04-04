package com.ondevice.mat.automation

import com.ondevice.mat.Test
import com.ondevice.mat.tests.*

/**
 * Contains all the tests cases licked to there packagename.
 * In this way we can easily add some tests for new packages
 */
class Explorer {

    private val testClasses: Map<String, Test> = mapOf(
        TicTacToeTest().packageName to TicTacToeTest(),
        SimplyToDoTest().packageName to SimplyToDoTest(),
        SudokuTest().packageName to SudokuTest(),
        TabletopToolsTest().packageName to TabletopToolsTest(),
        TippyTipperTest().packageName to TippyTipperTest()
    )

    fun targetTestClass(targetApk: String): Test? {

        if (testClasses.containsKey(targetApk)) {
            return testClasses[targetApk]
        }
        return null
    }

    fun getAvailableApplications(): List<String> {
        return testClasses.keys.toList()
    }


}