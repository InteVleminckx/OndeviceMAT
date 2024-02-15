package com.ondevice.mat.automation

import android.util.Log
import com.ondevice.mat.Test
import com.ondevice.mat.accessibility.MATAccessibilityService

class Automator(accessService: MATAccessibilityService) {

    private val TAG = "DebugTag"
    private val service: MATAccessibilityService = accessService
    private var applicationStarted: Boolean = false
    private val explorer: Explorer = Explorer()
    private val interactor: Interactor = Interactor(service)

    companion object {
        var targetApk: String = ""
    }

    fun openApplication() {
        Log.v(TAG, "Open: $targetApk")
        if (targetApk.isNotEmpty()) {
            val intent = service.packageManager.getLaunchIntentForPackage(targetApk)
            intent?.let {
                // Start the app using the intent
                service.startActivity(intent)

                // Wait until the app is loaded
                while (!appIsFullyLoaded()) {
                    continue
                }
                applicationStarted = true
            }
        }
    }

    private fun appIsFullyLoaded(): Boolean {
        val rootNode = service.rootInActiveWindow
        if (rootNode.packageName != targetApk) {
            return false
        }
        if (rootNode != null) {
            val contentView = rootNode.findAccessibilityNodeInfosByViewId("android:id/content")
            if (contentView.isEmpty()) {
                return false
            } else {
                return true
            }
        }
        return false
    }

    fun startTests() {
        if (!applicationStarted) {
            return
        }

        val testClass: Test = explorer.targetTestClass(targetApk) ?: return
        testClass.interactor = interactor
        testClass.runTests()

    }

}