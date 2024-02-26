package com.ondevice.mat.automation

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityNodeInfo
import com.ondevice.mat.accessibility.MATAccessibilityService
import kotlinx.coroutines.delay

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

    suspend fun click(node: NodeInfo) {
        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        delay(100)
    }

}