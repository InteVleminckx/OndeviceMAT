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

        observers.forEach { it.onAccessibilityEvent(event) }

        if (event?.packageName == this.packageName && !automator.applicationStarted) {
            if (EventFilter().checkEvent(event, EventFilter.events.CLICK, Automator.targetApk, true)) {
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

        observers.clear()

        // Cancel the coroutine scope when the service is destroyed
        cancel()
    }

}