package com.ondevice.mat

import android.util.Log
import com.ondevice.mat.automation.Interactor

open class Test {

    private val TAG = "DebugTag"
    open val packageName: String = ""
    open var interactor: Interactor? = null

    open fun runTests() {
        Log.v(TAG, "Start running tests for package: $packageName")
    }


}