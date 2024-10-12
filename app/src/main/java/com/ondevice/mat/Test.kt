package com.ondevice.mat

import android.os.Environment
import android.util.Log
import com.ondevice.mat.automation.Engine
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.reflect.KSuspendFunction1
import kotlin.system.measureTimeMillis

open class Test {

    private val TAG = "DebugTag"
    open val packageName: String = ""
    open var appName: String = ""
    open var engine: Engine? = null

    open val OUTPUT_TAG = "TestOutput"

    private val folderPath = createFolderIfNotExists()
    private var file: FileOutputStream? = null
    private var globalFile: FileOutputStream? = null
    private var totalExecutionTime: Double = 0.0

    open fun setup(automationEngine: Engine) {
        engine = automationEngine
    }

    open suspend fun runTests() {
        Log.v(TAG, "Start running tests for package: $packageName")
        file = createFile()
        globalFile = createGlobalFile()
        totalExecutionTime = 0.0
    }

    private fun createFolderIfNotExists(): String {
        val folderPath =
            Environment.getExternalStorageDirectory().toString() + "/Documents/TestLogs/"
        val folder = File(folderPath)

        if (!folder.exists()) {
            folder.mkdirs()
        }

        return folderPath
    }

    fun createNewFile() {
        file = createFile();
    }

    private fun createGlobalFile(): FileOutputStream? {
        val filename = "overview-$appName.txt"

        return try {
            val file: File = File(folderPath, filename)
            FileOutputStream(file, true)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createFile(): FileOutputStream? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val currentTimeStamp = dateFormat.format(Date())

        val fileName = "log-$appName-$currentTimeStamp.txt"

        try {
            val file: File = File(folderPath, fileName)
            return FileOutputStream(file, true)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun logToGlobalFile(message: String) {

        globalFile?.write("${message}\n".toByteArray())

    }

    fun logToFile(message: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTimeStamp = dateFormat.format(Date())
        val logMessage = "* $currentTimeStamp ----\n| $message\n"

        file?.write(logMessage.toByteArray())
    }

    suspend fun executeTest(
        func: KSuspendFunction1<Int, Pair<Boolean, String>>,
        iterations: Int,
        testName: String,
        countUpExecutions: Boolean
    ) {
        if (file == null) return

        Log.v(OUTPUT_TAG, "Starting test \"$testName\"")
        logToFile("Starting test '$testName'")
        var testResult: Pair<Boolean, String>
        val executionTime = measureTimeMillis {
            testResult = func(iterations)

            if (!testResult.first) {
                Log.v(OUTPUT_TAG, "Test \"$testName\" failed")
                logToFile(testResult.second)
            }
        }

        if (testResult.first) {
            Log.v(
                OUTPUT_TAG,
                "Finished the test with $iterations in ${executionTime / 1000.0} seconds"
            )
            logToFile("Finished the test with $iterations iterations in ${executionTime / 1000.0} seconds")
            if (!countUpExecutions) {
                totalExecutionTime += (executionTime / 1000.0)
                logToGlobalFile("Duration: $totalExecutionTime seconds")
            } else {
                totalExecutionTime += (executionTime / 1000.0)
            }
        }
    }


}