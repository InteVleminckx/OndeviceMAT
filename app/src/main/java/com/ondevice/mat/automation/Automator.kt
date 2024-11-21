package com.ondevice.mat.automation

import android.util.Log
import com.ondevice.mat.recorder.ScreenRecorderConnection
import com.ondevice.mat.accessibility.MATAccessibilityService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.E

class Automator(accessService: MATAccessibilityService) {

    private val service: MATAccessibilityService = accessService
    var applicationStarted: Boolean = false

    private val explorer: Explorer = Explorer()
    private val engine: Engine = Engine(service)

    private val waitingTime: Long = 500

    companion object {
        var targetApk: String = ""
        var permissionsChecked: Boolean = false
//        var screenRecorderConnection: ScreenRecorderConnection? = null
    }

    suspend fun start() {

        // Check if the screen recorder service is active and that all permissions are checked
//        if (screenRecorderConnection != null) {
//            if (permissionsChecked && screenRecorderConnection!!.isActive()) {
//                Log.v("DebugTag", "Waiting for target apk")
//                // Keeps checking until the target apk is set
//                waitForTargetApk()
//                Log.v("DebugTag", "Found target apk")
//                // sets up the engine, starts the application and waits until it is fully started
//                engine.setup(targetApk)
//                applicationStarted = true
//                // Start the tests for the application
//                startTests()
//            }
//        }
        if (permissionsChecked) {
            Log.v("DebugTag", "Waiting for target apk")
            // Keeps checking until the target apk is set
            waitForTargetApk()
            Log.v("DebugTag", "Found target apk")
            // sets up the engine, starts the application and waits until it is fully started
            engine.setup(targetApk)
            applicationStarted = true
            // Start the tests for the application
            startTests()
        }
    }

    private suspend fun waitForTargetApk() {
        while (targetApk == "") {
            delay(waitingTime)
        }
    }

    private suspend fun startTests() {
        if (!applicationStarted) {
            return
        }

        val testCase = explorer.targetTestClass(targetApk)
        if (testCase != null) {
            testCase.setup(engine)
            testCase.runTests()
        }
    }


}