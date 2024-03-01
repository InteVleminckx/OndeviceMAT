package com.ondevice.mat.automation

import android.view.accessibility.AccessibilityNodeInfo
import com.ondevice.mat.accessibility.MATAccessibilityService

class Parser(private val service: MATAccessibilityService) {

    private val parsedContent: MutableList<NodeInfo> = mutableListOf()

    /**
     * Parses the current window content and only saves the leaf nodes.
     * These nodes are the nodes we need to interact with the application
     */
    fun parseCurrentWindow() {
        parsedContent.clear()
        parse(service.rootInActiveWindow)
    }

    /**
     * A recursive parse function that will extract the leaf nodes from the ui content
     */
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

