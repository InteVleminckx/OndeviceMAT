package com.ondevice.mat.automation

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.ondevice.mat.accessibility.MATAccessibilityService

class Interactor(private val service: MATAccessibilityService) {

    fun pressHome() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
    }

    fun pressBack() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }

    fun pressRecentApps() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
    }

    private fun performClickOnTarget(target: String) {
        val rootNodeInfo: AccessibilityNodeInfo? = service.rootInActiveWindow
        rootNodeInfo?.let {

            val targetNode = findNodeByTarget(it, target)
            targetNode?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }
    private fun findNodeByTarget(rootNode: AccessibilityNodeInfo, target: String): AccessibilityNodeInfo? {
        for (i in 0 until rootNode.childCount) {
            val childNode = rootNode.getChild(i)

            if (childNode != null) {
                // Check based on text
                if (childNode.text != null && childNode.text.toString().contains(target)) {
                    return childNode

                }
                // Check based on id
                else if (childNode.viewIdResourceName == target) {
                    return childNode
                }

                val result = findNodeByTarget(childNode, target)
                if (result != null) {
                    return result
                }
            }
        }

        return null
    }

}