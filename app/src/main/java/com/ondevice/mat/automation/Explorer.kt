package com.ondevice.mat.automation

import com.ondevice.mat.Test
import com.ondevice.mat.tests.CalculatorTest

class Explorer {

    private val testClasses: Map<String, Test> = mapOf(
        CalculatorTest().packageName to CalculatorTest()
    )

    fun targetTestClass(targetApk: String): Test? {

        if (testClasses.containsKey(targetApk)) {
            return testClasses[targetApk]
        }
        return null
    }

}