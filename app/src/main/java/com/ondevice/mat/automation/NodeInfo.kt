package com.ondevice.mat.automation

import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Contains all necessary information about a component that easily can be accessed by the tester
 */
class NodeInfo(private val source: AccessibilityNodeInfo) {

    private fun stringConverter(input: Any?): String {
        if (input == null) {
            return ""
        }

        return input.toString()
    }

    fun nodeText(): String { return stringConverter(source.text) }

    fun nodeResourceId(): String { return stringConverter(source.viewIdResourceName) }

    fun nodeClass(): String { return stringConverter(source.className) }

    fun nodePackage(): String { return stringConverter(source.packageName) }

    fun nodeContentDescription(): String { return stringConverter(source.contentDescription) }

    fun nodeIsCheckable(): Boolean { return source.isCheckable }

    fun nodeIsChecked(): Boolean { return source.isChecked }

    fun nodeIsClickable(): Boolean { return source.isClickable }

    fun nodeIsEnabled(): Boolean { return source.isEnabled }

    fun nodeIsFocusable(): Boolean { return source.isFocusable }

    fun nodeIsScrollable(): Boolean { return source.isScrollable }

    fun nodeIsLongClickable(): Boolean { return source.isLongClickable }

    fun nodeIsPassword(): Boolean { return source.isPassword }

    fun nodeIsSelected(): Boolean { return source.isSelected }

    fun nodeBoundaries(): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val rect = Rect()
        source.getBoundsInScreen(rect)
        val topLeft: Pair<Int, Int> = Pair(rect.top, rect.left)
        val bottomRight: Pair<Int, Int> = Pair(rect.bottom, rect.right)
        return Pair(topLeft, bottomRight)
    }

    fun performAction(action: Int) {
        source.performAction(action)
    }

}