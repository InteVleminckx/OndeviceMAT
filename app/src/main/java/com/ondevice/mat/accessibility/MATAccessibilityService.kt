package com.ondevice.mat.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.ondevice.mat.automation.Automator
import kotlinx.coroutines.*

class MATAccessibilityService : AccessibilityService(), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private val TAG = "DebugTag"
    private val observers = mutableListOf<AccessibilityEventListener>()

    private val automator: Automator = Automator(this)


    fun addObserver(observer: AccessibilityEventListener) {
        observers.add(observer)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        // Notify all observers with this accessibility event
        observers.forEach { it.onAccessibilityEvent(event) }

        // Check an application is selected in the overview and check of no application is started
        if (event?.packageName == this.packageName && !automator.applicationStarted) {
            // Check if it is a click event and an apk has been selected
            if (EventFilter().checkEvent(event, EventFilter.events.CLICK, Automator.targetApk, true)) {
                // Start the automator with a coroutine scope
                launch {
                    automator.start()
                }
            }
        }

    }

    override fun onInterrupt() {
        Log.v(TAG, "onInterrupt")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.v(TAG, "onServiceConnected")
    }

    override fun onDestroy() {
        super.onDestroy()

        // Removes all the observers
        observers.clear()

        // Cancel the coroutine scope when the service is destroyed
        cancel()
    }

}