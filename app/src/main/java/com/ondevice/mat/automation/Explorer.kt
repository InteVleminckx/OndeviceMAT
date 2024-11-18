package com.ondevice.mat.automation

import com.ondevice.mat.Test
import com.ondevice.mat.tests.*
import com.ondevice.offdevicetesting.CalendarTest
import com.ondevice.offdevicetesting.DictionaryTest
import com.ondevice.offdevicetesting.ExpenseTrackerTest
import com.ondevice.offdevicetesting.NewsTest

/**
 * Contains all the tests cases licked to there packagename.
 * In this way we can easily add some tests for new packages
 */
class Explorer {

    private val testClasses: Map<String, Test> = mapOf(
        TicTacToeTest().packageName to TicTacToeTest(),
        SimpleDoTest().packageName to SimpleDoTest(),
        SudokuTest().packageName to SudokuTest(),
        TippyTipperTest().packageName to TippyTipperTest(),
        CalendarTest().packageName to CalendarTest(),
        ExpenseTrackerTest().packageName to ExpenseTrackerTest(),
        DictionaryTest().packageName to DictionaryTest(),
        NewsTest().packageName to NewsTest(),
        FriendzoneTest().packageName to FriendzoneTest()
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