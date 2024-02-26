package com.ondevice.mat.accessibility

import android.view.accessibility.AccessibilityEvent

interface AccessibilityEventListener {
    fun onAccessibilityEvent(event: AccessibilityEvent?)

}