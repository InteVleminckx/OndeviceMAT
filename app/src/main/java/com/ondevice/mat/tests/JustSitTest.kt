package com.ondevice.mat.tests

import android.util.Log
import com.ondevice.mat.Test
import com.ondevice.mat.automation.Engine
import com.ondevice.mat.automation.NodeInfo

class JustSitTest : Test() {

    override val packageName = "com.brocktice.JustSit"

    override suspend fun runTests() {
        super.runTests()

        if (engine != null) {
            testJustSit()
        }

    }

    private suspend fun testJustSit() {

        val times: List<String> = listOf("9", "8", "7")

        for (time in times) {
            val prepDown: NodeInfo = engine?.retrieveNode("prep_down_button", Engine.searchTypes.RESOURCE_ID) ?: return

            val clickSucceeded =
                engine?.click(prepDown, Pair("prep_down_button", Engine.searchTypes.RESOURCE_ID)) == true

            if (!clickSucceeded) {
                Log.v("DebugTag", "Click failed")

            } else {

                val preparationText: NodeInfo = engine?.retrieveNode("preparation_text", Engine.searchTypes.RESOURCE_ID) ?: return

                if (preparationText.nodeText() != time) {
                    Log.v("DebugTag", "Preparation time didn't change")
                } else {
                    Log.v("DebugTag", "Preparation time changed")
                }
            }
        }
    }

}