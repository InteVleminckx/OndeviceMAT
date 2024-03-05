package com.ondevice.mat.automation

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.ondevice.mat.accessibility.MATAccessibilityService
import kotlinx.coroutines.delay

class Interactor(private val service: MATAccessibilityService) {

    private val SWIPE_DISTANCE = 200

    private data class Coordinates(val startX: Float, val startY: Float, val endX: Float, val endY: Float)

    enum class SwipeDirection {
        LEFT, RIGHT, UP, DOWN
    }

    suspend fun pressHome() {
        // Press the home button
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
        delay(2500)
    }

    fun pressBack() {
        // Press the back button
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }

    fun pressRecentApps() {
        // Press the recent apps button
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS)
    }

    suspend fun click(node: NodeInfo) {

        // Performs a click event
        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        delay(100)
    }

    private fun swipeCoordinates(direction: SwipeDirection): Coordinates {
        val displayMetrics = service.resources.displayMetrics

        return when (direction) {
            SwipeDirection.UP -> {
                val startX = displayMetrics.widthPixels / 2
                val endX = startX
                val startY = displayMetrics.heightPixels * 5 / 8
                val endY = displayMetrics.heightPixels * 3 / 8
                Coordinates(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat())
            }

            SwipeDirection.DOWN -> {
                val startX = displayMetrics.widthPixels / 2
                val endX = startX
                val startY = displayMetrics.heightPixels * 3 / 8
                val endY = displayMetrics.heightPixels * 5 / 8
                Coordinates(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat())
            }

            SwipeDirection.LEFT -> {
                val startX = displayMetrics.widthPixels * 3 / 4
                val endX = displayMetrics.widthPixels / 4
                val startY = displayMetrics.heightPixels / 2
                val endY = displayMetrics.heightPixels / 2
                Coordinates(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat())
            }

            SwipeDirection.RIGHT -> {
                val startX = displayMetrics.widthPixels / 4
                val endX = displayMetrics.widthPixels * 3 / 4
                val startY = displayMetrics.heightPixels / 2
                val endY = displayMetrics.heightPixels / 2
                Coordinates(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat())
            }
        }
    }

    suspend fun swipe(direction: SwipeDirection) {

        val path = Path()
        val coordinates: Coordinates = swipeCoordinates(direction)
        path.moveTo(coordinates.startX, coordinates.startY)
        path.lineTo(coordinates.endX, coordinates.endY)

        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100)) // Duration in milliseconds

        service.dispatchGesture(gestureBuilder.build(), null, null)
        delay(200)

    }

}