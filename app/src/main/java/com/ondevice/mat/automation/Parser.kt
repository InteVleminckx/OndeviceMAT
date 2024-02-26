package com.ondevice.mat.automation

import android.view.accessibility.AccessibilityNodeInfo
import com.ondevice.mat.accessibility.MATAccessibilityService

class Parser(private val service: MATAccessibilityService) {

    private val parsedContent: MutableList<NodeInfo> = mutableListOf()

    fun parseCurrentWindow() {
        parsedContent.clear()
        parse(service.rootInActiveWindow)
    }

    private fun parse(node: AccessibilityNodeInfo) {

        if (node.childCount == 0) {
            parsedContent.add(NodeInfo(node))
        }

        for (i in 0..node.childCount) {

            if (i < node.childCount) {
                val child = node.getChild(i)
                parse(child)
            }
        }
    }

    fun getParsedContent(): List<NodeInfo> {
        return parsedContent
    }

}

