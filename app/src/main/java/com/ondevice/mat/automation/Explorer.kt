package com.ondevice.mat.automation

import com.ondevice.mat.Test
import com.ondevice.mat.tests.JustSitTest
import com.ondevice.mat.tests.TicTacToeTest

/**
 * Contains all the tests cases licked to there packagename.
 * In this way we can easily add some tests for new packages
 */
class Explorer {

    private val testClasses: Map<String, Test> = mapOf(
        TicTacToeTest().packageName to TicTacToeTest(),
        JustSitTest().packageName to JustSitTest()
    )

    fun targetTestClass(targetApk: String): Test? {

        if (testClasses.containsKey(targetApk)) {
            return testClasses[targetApk]
        }
        return null
    }

}