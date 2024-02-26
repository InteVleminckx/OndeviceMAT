package com.ondevice.mat.automation;

import android.text.BoringLayout
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.ondevice.mat.accessibility.EventFilter
import com.ondevice.mat.accessibility.EventListener
import com.ondevice.mat.accessibility.MATAccessibilityService;
import kotlinx.coroutines.*

class Engine(private val service: MATAccessibilityService) {

    private val eventListener: EventListener = EventListener(service)
    private val parser: Parser = Parser(service)
    private val interactor: Interactor = Interactor(service)

    private var targetApk: String = ""

    private val startUpDelay: Long = 10000
    private val timeoutTime: Long = 10000
    private val checkDelay: Long = 50

    enum class searchTypes {
        TEXT, RESOURCE_ID
    }

    suspend fun setup(apk: String) {

        targetApk = apk
        eventListener.setTargetApk(targetApk)

        openApplication()

        Log.v("DebugTag", "Application started: $targetApk")

    }

    private suspend fun openApplication() {
        val intent = service.packageManager.getLaunchIntentForPackage(targetApk)
        intent?.let {
            // Start the app using the intent
            service.startActivity(intent)
        }

        delay(startUpDelay)
    }

    private fun retrieveWindowContent(): List<NodeInfo> {
        parser.parseCurrentWindow()
        return parser.getParsedContent()
    }

    fun retrieveNode(searchTerm: String, searchType: searchTypes): NodeInfo? {
        val windowContent: List<NodeInfo> = retrieveWindowContent()
        return windowContent.firstOrNull { node ->
            when (searchType) {
                searchTypes.TEXT -> node.nodeText().contains(searchTerm)
                searchTypes.RESOURCE_ID -> node.nodeResourceId().contains(searchTerm)
            }
        }
    }

    private suspend fun checkInteractionSucceeded(): Boolean {

        return try {
            withTimeout(timeoutTime) {
                while (!eventListener.eventIsPerformed()) {
                    delay(checkDelay)
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun checkWindowStoppedChanging(): Boolean {
        return try {
            withTimeout(timeoutTime) {
                while (!eventListener.windowContentStoppedChanging()) {
                    delay(checkDelay)
                }
            }
            true

        } catch (e: Exception) {
            false
        }
    }


    suspend fun click(
        node: NodeInfo,
        target: Pair<String, searchTypes>,
        changesScreenContent: Boolean = true
    ): Boolean {

        if (!node.nodeIsClickable()) {
            Log.v("DebugTag", "Node isn't clickable")
            return false
        }

        // Notify listener that some interaction will occur
        eventListener.setExpectedEvent(EventFilter.events.CLICK)

        // Perform interaction
        interactor.click(node)

        // Check if interaction found place using the listener
        var isInteractionSucceeded = checkInteractionSucceeded()
        Log.v("DebugTag", "Interaction succeed: $isInteractionSucceeded")


        // If the content of the screen need to be changed, check this happened and it stopped changing
        if (changesScreenContent) {
            if (checkWindowStoppedChanging()) {
                Log.v("DebugTag", "Windows stopped changing")
                isInteractionSucceeded = eventListener.windowContentHasBeenChanged()
                Log.v("DebugTag", "Window has been changed: $isInteractionSucceeded")
            }
        }

        // Reset the parameters for the next interaction
        eventListener.resetExpected()

        // Check if the current window contains the target
        isInteractionSucceeded = retrieveNode(target.first, target.second) != null
        Log.v("DebugTag", "Window contains target: $isInteractionSucceeded")


        // Let the tester now that the click is succeeded
        return isInteractionSucceeded
    }


}
