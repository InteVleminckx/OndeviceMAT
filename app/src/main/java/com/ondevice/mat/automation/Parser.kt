package com.ondevice.mat.automation

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.ondevice.mat.accessibility.MATAccessibilityService
import org.json.JSONException

class Parser(private val service: MATAccessibilityService) {

    private val parsedContent: MutableList<NodeInfo> = mutableListOf()
    private val allNodes: MutableList<NodeInfo> = mutableListOf()

    /**
     * Parses the current window content and only saves the leaf nodes.
     * These nodes are the nodes we need to interact with the application
     */
    fun parseCurrentWindow() {
        parsedContent.clear()
        allNodes.clear()
        parse(service.rootInActiveWindow)
    }

    /**
     * A recursive parse function that will extract the leaf nodes from the ui content
     */
    private fun parse(node: AccessibilityNodeInfo) {

        try {
            val nodeInfo = NodeInfo(node)

            allNodes.add(nodeInfo)

            if (node.childCount == 0) {
                parsedContent.add(nodeInfo)
            }

            for (i in 0..node.childCount) {

                if (i < node.childCount) {
                    val child = node.getChild(i) ?: continue
                    parse(child)
                }
            }

        } catch (e: JSONException) {
            e.printStackTrace()
            return
        }


    }

    fun getParsedContent(): List<NodeInfo> {
        return parsedContent
    }

    fun getAllNodes(): List<NodeInfo> {
        return allNodes
    }

    fun findObjectByClassName(className: String): List<NodeInfo> {

        val nodes: MutableList<NodeInfo> = mutableListOf();

        for (node in getAllNodes()) {
           if (node.nodeClass() == className) {
               nodes.add(node)
           }
        }

        return nodes;

    }



}

