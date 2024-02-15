package com.ondevice.mat.accessibility

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.ondevice.mat.automation.Automator

class MATAccessibilityService : AccessibilityService() {

    private val TAG = "DebugTag"
    private val eventFilter: EventFilter = EventFilter()
    private val automator: Automator = Automator(this)
    private val startUpDelay: Long = 5000

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        val eventInfo: Map<String, String> = eventFilter.checkEvent(event)

        val eventType: String = eventInfo["eventType"].toString()

        if (eventType == "invalid") {
            return
        }

        val packageName: String = eventInfo["packageName"].toString()
        val eventText: String = eventInfo["eventText"].toString()

        if (eventType == "WindowStateChanged") {

        }

//        Log.v(TAG, eventInfo.toString())


    }

    override fun onInterrupt() {
        Log.v(TAG, "onInterrupt")
    }

    override fun onServiceConnected() {
        Log.v(TAG, "onServiceConnected")
        automator.openApplication()
        Handler(Looper.getMainLooper()).postDelayed({
            automator.startTests()
        }, startUpDelay)

    }



}