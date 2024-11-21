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

    fun fastSearch(searchTerm: String, searchTypes: Engine.searchTypes, node: NodeInfo? = null): NodeInfo? {
        try {

            var nodeInfo = node
            if (nodeInfo == null) {
                nodeInfo = NodeInfo(service.rootInActiveWindow)
            }

            val found = when (searchTypes) {
                Engine.searchTypes.TEXT -> nodeInfo.nodeText().contains(searchTerm)
                Engine.searchTypes.RESOURCE_ID -> nodeInfo.nodeResourceId().contains(searchTerm)
                Engine.searchTypes.CONTENT_DESC -> nodeInfo.nodeContentDescription().contains(searchTerm)
            }

            if (found) {
                return nodeInfo
            }

            for (i in 0 until nodeInfo.childCount()) {
                val result = fastSearch(searchTerm, searchTypes, nodeInfo.getChild(i))
                if (result != null) {
                    return result
                }
            }

        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }

        return null
    }


}

