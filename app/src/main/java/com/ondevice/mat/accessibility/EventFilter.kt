package com.ondevice.mat.accessibility

import android.annotation.SuppressLint
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class EventFilter {

    private val TAG = "DebugTag"


    enum class events {
        CLICK, LONG_CLICK, SCROLL, SELECT, CHANGE_TEXT, CHANGE_TEXT_SELECTION, CHANGE_WINDOW, CHANGE_WINDOW_CONTENT, CHANGE_WINDOW_STATE, NONE
    }

    @SuppressLint("SwitchIntDef")
    fun checkEvent(
        event: AccessibilityEvent?, expectedType: events, expectedPackage: String, isStartPackage: Boolean = false
    ): Boolean {
        if (event == null) {
            return false
        }

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> return check(
                event, events.CLICK, expectedType, expectedPackage, isStartPackage
            )

            AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> return check(
                event, events.LONG_CLICK, expectedType, expectedPackage, isStartPackage
            )

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> return check(
                event, events.SCROLL, expectedType, expectedPackage, isStartPackage
            )

            AccessibilityEvent.TYPE_VIEW_SELECTED -> return check(
                event, events.SELECT, expectedType, expectedPackage, isStartPackage
            )

            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> return check(
                event, events.CHANGE_TEXT, expectedType, expectedPackage, isStartPackage
            )

            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> return check(
                event, events.CHANGE_TEXT_SELECTION, expectedType, expectedPackage, isStartPackage
            )

            AccessibilityEvent.TYPE_WINDOWS_CHANGED -> return check(
                event, events.CHANGE_WINDOW, expectedType, expectedPackage, isStartPackage
            )

            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> return check(
                event, events.CHANGE_WINDOW_CONTENT, expectedType, expectedPackage, isStartPackage
            )

            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> return check(
                event, events.CHANGE_WINDOW_STATE, expectedType, expectedPackage, isStartPackage
            )
        }

        return false
    }

    private fun check(
        event: AccessibilityEvent,
        checkType: events,
        expectedType: events,
        expectedPackage: String,
        isStartPackage: Boolean = false
    ): Boolean {

        if (isStartPackage) {
            return checkType == expectedType && retrieveStartPackageName(event) == expectedPackage
        }

        Log.v("DebugTag", "$checkType - ${event.packageName}")

        return checkType == expectedType && event.packageName == expectedPackage

    }


    private fun retrieveStartPackageName(event: AccessibilityEvent): String {
        val text: List<CharSequence> = event.text

        if (text.isEmpty()) {
            return ""
        }

        return text.joinToString(" ")
    }


}