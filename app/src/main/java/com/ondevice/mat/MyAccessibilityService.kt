package com.ondevice.mat

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent


class MyAccessibilityService : AccessibilityService() {

    private val TAG = "DebugTag"

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packName: String = event?.packageName.toString()
        Log.v(TAG, "Event : ${event.toString()}")
        Log.v(TAG, "onAccessibilityEvent : $packName")
    }

    override fun onInterrupt() {
        Log.v(TAG, "onInterrupt")
    }

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo()

        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED or
                AccessibilityEvent.TYPE_VIEW_FOCUSED

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN

        this.serviceInfo = info

        Log.v(TAG, "onServiceConnected")
    }

}