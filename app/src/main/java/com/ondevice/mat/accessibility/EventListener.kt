package com.ondevice.mat.accessibility

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
        if (eventExecutionTime != null) {

            return contentChangedTime?.isAfter(eventExecutionTime) == true
        }

        return false

    }

    fun windowContentStoppedChanging(): Boolean {
        if (contentChangedTime == null) {
            return true
        }

        val currentTime: LocalTime = LocalTime.now()
        val difference = ChronoUnit.MILLIS.between(contentChangedTime, currentTime)

        return difference > stopChangingDelay

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (targetApk == "") {
            return
        }

        if (expectedEvent != EventFilter.events.NONE && !eventPerformed) {

            val windowContentChanged =
                eventFilter.checkEvent(event, EventFilter.events.CHANGE_WINDOW_CONTENT, targetApk)

            if (!windowContentChanged) {
                eventPerformed = eventFilter.checkEvent(event, expectedEvent, targetApk)
            } else {
                contentChangedTime = LocalTime.now()
            }
        }

    }
}