package com.ondevice.mat

import android.util.Log
import com.ondevice.mat.automation.Engine
import com.ondevice.mat.automation.Interactor
import java.io.FileWriter
import java.io.PrintWriter

open class Test {

    private val TAG = "DebugTag"
    open val packageName: String = ""
    open var engine: Engine? = null

    open val OUTPUT_TAG = "TestOutput"

    open fun setup(automationEngine: Engine) {
        engine = automationEngine
    }

    open suspend fun runTests() {
        Log.v(TAG, "Start running tests for package: $packageName")
    }




}