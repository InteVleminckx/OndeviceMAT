package com.ondevice.mat.accessibility

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class EventListener(service: MATAccessibilityService) : AccessibilityEventListener {

    private val eventFilter: EventFilter = EventFilter()
    private var targetApk: String = ""
    private var expectedEvent: EventFilter.events = EventFilter.events.NONE
    private var eventPerformed: Boolean = false

    private var contentChangedTime: LocalTime? = null
    private var eventExecutionTime: LocalTime? = null

    private val stopChangingDelay: Long = 500

    init {
        service.addObserver(this)
    }

    fun setTargetApk(apk: String) {
        targetApk = apk
    }

    fun setExpectedEvent(eventType: EventFilter.events) {
        // Gets an event that will be expected to execute
        // Saves the execution time, this will be used to check if some changes has been applied after performing
        // a new interaction
        eventExecutionTime = LocalTime.now()
        expectedEvent = eventType
    }

    fun eventIsPerformed(): Boolean {
        return eventPerformed
    }

    fun resetExpected() {
        expectedEvent = EventFilter.events.NONE
        eventPerformed = false
        eventExecutionTime = null
    }

    fun windowContentHasBeenChanged(): Boolean {
        // If there is no execution time, no event has be set / occurred
        if (eventExecutionTime != null) {
            // Check if the content has changed after the execution time of the event
            return contentChangedTime?.isAfter(eventExecutionTime) == true
        }

        return false

    }

    fun windowContentStoppedChanging(): Boolean {
        if (contentChangedTime == null) {
            return true
        }

        // If the last time the content of the window has been changed outside some certain changing delay
        // We can say that the window has been stopped changing
        val currentTime: LocalTime = LocalTime.now()
        val difference = ChronoUnit.MILLIS.between(contentChangedTime, currentTime)

        return difference > stopChangingDelay

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (targetApk == "") {
            return
        }

        Log.v("DebugTag", event.toString())

        if (expectedEvent != EventFilter.events.NONE && !eventPerformed) {

            // Checks if the window content has been changed
            val windowContentChanged =
                eventFilter.checkEvent(event, EventFilter.events.CHANGE_WINDOW_CONTENT, targetApk)

            // If this isn't the case, another event can be occurred.
            if (!windowContentChanged) {
                // Contains a boolean indicating the expected event has been occurred or not
                eventPerformed = eventFilter.checkEvent(event, expectedEvent, targetApk)
            } else {
                // Update content changing time
                contentChangedTime = LocalTime.now()
            }
        }

    }
}