package com.ondevice.mat.automation

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityNodeInfo
import com.ondevice.mat.accessibility.MATAccessibilityService
import kotlinx.coroutines.delay

class Interactor(private val service: MATAccessibilityService) {

    fun pressHome() {
        // Press the home button
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
    }

    fun pressBack() {
        // Press the back button
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }

    fun pressRecentApps() {
        // Press the recent apps button
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
    }

    suspend fun click(node: NodeInfo) {
        // Performs a click event
        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        delay(100)
    }

}