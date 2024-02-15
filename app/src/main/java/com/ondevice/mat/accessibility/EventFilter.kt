package com.ondevice.mat.accessibility

import android.util.Log
import android.view.accessibility.AccessibilityEvent

class EventFilter {

    private val TAG = "DebugTag"

    fun checkEvent(event: AccessibilityEvent?): Map<String, String> {
//        Log.v(TAG, event.toString())
        if (event == null) {
            return invalidMap()
        }

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            return viewClicked(event)
        } else if (event.eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            return viewFocused(event)
        } else if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return windowStateChanged(event)
        }

        return invalidMap()
    }

    private fun invalidMap(): Map<String, String> {
        return mapOf(
            "eventType" to "invalid"
        )
    }

    private fun viewClicked(event: AccessibilityEvent): Map<String, String> {
        return eventInfo(event, "ViewClicked")
    }

    private fun viewFocused(event: AccessibilityEvent): Map<String, String> {
        return eventInfo(event, "ViewFocused")
    }

    private fun windowStateChanged(event: AccessibilityEvent): Map<String, String> {
        return eventInfo(event, "WindowStateChanged")
    }

    private fun eventInfo(event: AccessibilityEvent, eventType: String): Map<String, String> {
        return mapOf(
            "eventType" to eventType,
            "packageName" to retrievePackageName(event),
            "eventText" to retrieveEventText(event)
        )
    }

    private fun retrieveEventText(event: AccessibilityEvent): String {

        val text: List<CharSequence> = event.text

        if (text.isEmpty()) {
            return ""
        }

        return text.joinToString(" ")
    }

    private fun retrievePackageName(event: AccessibilityEvent): String {
        return event.packageName.toString()
    }


}