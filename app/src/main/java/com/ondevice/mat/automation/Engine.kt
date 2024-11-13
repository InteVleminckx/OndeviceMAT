package com.ondevice.mat.automation;

import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.ondevice.mat.accessibility.EventFilter
import com.ondevice.mat.accessibility.EventListener
import com.ondevice.mat.accessibility.MATAccessibilityService
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout


class Engine(private val service: MATAccessibilityService) {

    private val eventListener: EventListener = EventListener(service)
    private val parser: Parser = Parser(service)
    private val interactor: Interactor = Interactor(service)

    private var targetApk: String = ""

    private val startUpDelay: Long = 10000
    private val timeoutTime: Long = 10000
    private val checkDelay: Long = 50

    enum class searchTypes {
        TEXT, RESOURCE_ID, CONTENT_DESC
    }

    suspend fun setup(apk: String) {

        targetApk = apk
        // Let the event listener know which apk he must listen to
        eventListener.setTargetApk(targetApk)

        // Start the apk with a start-up delay
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

    /**
     * Returns content that is available in the current window
     */
    private fun retrieveWindowContent(): List<NodeInfo> {
        parser.parseCurrentWindow()
        return parser.getParsedContent()
    }

    private fun getAllNodes(): List<NodeInfo> {
        parser.parseCurrentWindow()
        return parser.getAllNodes()
    }

    fun findObjectByClassName(className: String): List<NodeInfo> {
       parser.parseCurrentWindow();
       return parser.findObjectByClassName(className);
    }

    fun retrieveNode(searchTerm: String, searchType: searchTypes, allNodes: Boolean = false): NodeInfo? {
        // First retrieve the content from the window
        val windowContent: List<NodeInfo> = if (!allNodes) { retrieveWindowContent() } else { getAllNodes() }
            // Searches the component on the current window based on a particular search type and term

        return windowContent.firstOrNull { node ->
            when (searchType) {
                searchTypes.TEXT -> node.nodeText().contains(searchTerm)
                searchTypes.RESOURCE_ID -> node.nodeResourceId().contains(searchTerm)
                searchTypes.CONTENT_DESC -> node.nodeContentDescription().contains(searchTerm)
            }
        }
    }

    /**
     * Keeps checking if a particular interaction has been performed for a certain time.
     * If the timeout passed by, we say that the interaction doesn't succeed
     */
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

    /**
     * Keeps checking if the window content is still changing or not.
     * If it keeps changing for longer than the timeout time, we say that the window doesn't stop changing
     */
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

    /**
     * Keeps checking if the target node is visible on the screen.
     * If it keeps changing for longer than the timeout time, we say that the target node isn't on the screen
     */
    private suspend fun checkTargetNodeVisible(searchTerm: String, searchType: searchTypes): Boolean {
        return try {
            withTimeout(timeoutTime) {
                while (retrieveNode(searchTerm, searchType) == null) {
                    delay(checkDelay)
                }
            }
            true

        } catch (e: Exception) {
            false
        }
    }

    private suspend fun checkTextBoxContent(requiredText: String, node: NodeInfo): Boolean {
        return try {
            withTimeout(timeoutTime) {
                while (node.nodeText() != requiredText) {
                    delay(checkDelay)
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }


    /***
     * Performs a click event on a particular component.
     * @param node: the component that will be clicked
     * @param target: to confirm if the click event did the right thing, we use a target component that must be visible on the screen after the click event occurred
     * @param changesScreenContent: indicates if the screen content changes after performing some click
     */
    suspend fun click(
        node: NodeInfo,
        target: Pair<String, searchTypes>,
        changesScreenContent: Boolean = true,
        coordinates: Boolean = false
    ): Boolean {

        var cur_node = node
        // First check if the node is even clickable
        if (!cur_node.nodeIsClickable()) {
            if (!cur_node.getParent()?.nodeIsClickable()!!) {
                Log.v("DebugTag", "Node isn't clickable")
                return false
            }
            cur_node = node.getParent()!!
        }

        // Notify listener that some interaction will occur
        eventListener.setExpectedEvent(EventFilter.events.CLICK)

        // Perform interaction
        if (coordinates) {
            interactor.clickCoordinates(cur_node)
        }
        else {
            interactor.click(cur_node)
        }

        // Check if interaction found place using the listener
        var isInteractionSucceeded = checkInteractionSucceeded()
        Log.v("DebugTag", "Interaction succeed: $isInteractionSucceeded")

        // If the content of the screen need to be changed, check this happened and it stopped changing
        if (changesScreenContent && isInteractionSucceeded) {
            if (checkWindowStoppedChanging()) {
                Log.v("DebugTag", "Windows stopped changing")
                isInteractionSucceeded = eventListener.windowContentHasBeenChanged()
                Log.v("DebugTag", "Window has been changed: $isInteractionSucceeded")
            }
        }

        // Reset the parameters for the next interaction
        eventListener.resetExpected()

        // Check if the current window contains the target
        isInteractionSucceeded = retrieveNode(target.first, target.second, allNodes = true) != null
        Log.v("DebugTag", "Window contains target: $isInteractionSucceeded")

        delay(100)

        // Let the tester now that the click is succeeded
        return isInteractionSucceeded
    }


    suspend fun removeClick(
        node: NodeInfo,
        target: Pair<String, searchTypes>,
        changesScreenContent: Boolean = true,
        coordinates: Boolean = false
    ): Boolean {

        // First check if the node is even clickable
        if (!node.nodeIsClickable()) {
            Log.v("DebugTag", "Node isn't clickable")
            return false
        }

        // Notify listener that some interaction will occur
        eventListener.setExpectedEvent(EventFilter.events.SCROLL)

        // Perform interaction
        if (coordinates) {
            interactor.clickCoordinates(node)
        }
        else {
            interactor.click(node)
        }

        // Check if interaction found place using the listener
        var isInteractionSucceeded = checkInteractionSucceeded()
        Log.v("DebugTag", "Interaction succeed: $isInteractionSucceeded")

        // If the content of the screen need to be changed, check this happened and it stopped changing
        if (changesScreenContent && isInteractionSucceeded) {
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

        delay(100)

        // Let the tester now that the click is succeeded
        return isInteractionSucceeded
    }

    suspend fun swipe(direction: Interactor.SwipeDirection, target: Pair<String, searchTypes>? = null): Boolean {

        interactor.swipe(direction)

        if (target == null) {
            return true
        }

        return checkTargetNodeVisible(target.first, target.second)


    }

    suspend fun pressHome() {
        interactor.pressHome()
    }

    suspend fun pressBack(target: Pair<String, searchTypes>): Boolean {

        interactor.pressBack()

        var isInteractionSucceeded: Boolean
        if (checkWindowStoppedChanging()) {
            Log.v("DebugTag", "Windows stopped changing")
            isInteractionSucceeded = eventListener.windowContentHasBeenChanged()
            Log.v("DebugTag", "Window has been changed: $isInteractionSucceeded")
        }

        isInteractionSucceeded = retrieveNode(target.first, target.second) != null
        Log.v("DebugTag", "Window contains target: $isInteractionSucceeded")

        return isInteractionSucceeded

    }

    suspend fun longClick(
        node: NodeInfo,
        target: Pair<String, searchTypes>,
        changesScreenContent: Boolean = true
    ): Boolean {

        // First check if the node is even clickable
        if (!node.nodeIsLongClickable()) {
            Log.v("DebugTag", "Node isn't clickable")
            return false
        }

        // Notify listener that some interaction will occur
        eventListener.setExpectedEvent(EventFilter.events.LONG_CLICK)

        // Perform interaction
        interactor.longClick(node)

        // Check if interaction found place using the listener
        var isInteractionSucceeded = checkInteractionSucceeded()
        Log.v("DebugTag", "Interaction succeed: $isInteractionSucceeded")

        // If the content of the screen need to be changed, check this happened and it stopped changing
        if (changesScreenContent && isInteractionSucceeded) {
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

        delay(100)

        // Let the tester now that the click is succeeded
        return isInteractionSucceeded
    }


    suspend fun fillTextBox(node: NodeInfo, text: String, searchTerm: String, searchType: searchTypes): Boolean {
        interactor.setText(node, text)
        val editedNode = retrieveNode(searchTerm, searchType) ?: return false
        val result = checkTextBoxContent(text, editedNode)

        delay(100)

        return result
    }
}
