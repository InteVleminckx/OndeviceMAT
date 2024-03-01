package com.ondevice.mat.accessibility

import android.view.accessibility.AccessibilityEvent

/**
 * An interface class for an event listener
 */
interface AccessibilityEventListener {
    fun onAccessibilityEvent(event: AccessibilityEvent?)

}