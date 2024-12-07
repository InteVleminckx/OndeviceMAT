package com.ondevice.offdevicetesting

import android.os.Environment
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction1
import kotlin.system.measureTimeMillis

open class BaseTestClass(name: String) {

    var packageName: String
    var device: UiDevice
    val timeout: Long = 10000L
    private val testTag: String = "testTag"
    private val folderPath: String
    private var file: FileOutputStream?
    private var globalFile: FileOutputStream?

    init {
        packageName = name
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        folderPath = createFolderIfNotExists()
        file = createFile()
        globalFile = createGlobalFile()

    }

    private fun createFolderIfNotExists(): String {

        val folderPath = Environment.getExternalStorageDirectory().toString() + "/Documents/TestLogs/"
        val folder = File(folderPath)

        if (!folder.exists()) {
            folder.mkdirs()
        }

        return folderPath

    }

    private fun createGlobalFile(): FileOutputStream? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val currentTimeStamp = dateFormat.format(Date())

        val fileName = "overview-$packageName-$currentTimeStamp.txt"

        return try {
            val file: File = File(folderPath, fileName)
            FileOutputStream(file, true)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun createFile(): FileOutputStream? {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        val currentTimeStamp = dateFormat.format(Date())

        val fileName = "log-$packageName-$currentTimeStamp.txt"

        return try {
            val file: File = File(folderPath, fileName)
            FileOutputStream(file, true)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    fun createNewFile() {
        file = createFile();
    }


    private fun logToGlobalFile(message: String) {

        globalFile?.write("${message}\n".toByteArray())

    }

    fun logToFile(message: String) {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentTimeStamp = dateFormat.format(Date())
        val logMessage = "* $currentTimeStamp ----\n| $message\n"

        file?.write(logMessage.toByteArray())

    }

    fun executeTest(
        func: KFunction1<Int, Pair<Boolean, String>>,
        iterations: Int,
        testName: String
    ): String {

        if (file == null) return "Log file not found."

        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        Log.v(testTag, "Starting test \"$testName\"")
        logToFile("Starting test '$testName'")
        var testResult: Pair<Boolean, String>
        val executionTime = measureTimeMillis {

            testResult = func(iterations)

            if (!testResult.first) {
                Log.v(testTag, "Test \"$testName\" failed")
                logToFile(testResult.second)
                return testResult.second;
            }
        }

        if (testResult.first) {
            Log.v(
                testTag,
                "Finished the test with $iterations in ${executionTime / 1000.0} seconds"
            )
            logToFile("Finished the test with $iterations iterations in ${executionTime / 1000.0} seconds")
            logToGlobalFile("Duration: ${executionTime / 1000.0} seconds")
        }

        return testResult.second;
    }

    fun getResId(id: String): String {
        return "$packageName:id/$id"
    }

    fun clickObjectByRes(res: String): Pair<Boolean, String> {

        return try {
            device.wait(Until.hasObject(By.res(res)), timeout)

            val obj = device.findObject(By.res(res))
            obj.click()

            Pair(true, "Successfully clicked object with resource id: $res")

        } catch (e: Exception) {

//            Log.v(testTag, e.toString())
            Log.v(testTag, "Couldn't click object with resource id: $res")
            Pair(true, "Couldn't click object with resource id: $res")
        }
    }

    fun clickObjectByText(text: String): Pair<Boolean, String> {

        return try {
            device.wait(Until.hasObject(By.text(text)), timeout)

            val obj = device.findObject(By.text(text))
            obj.click()

            Pair(true, "Successfully clicked object with text: $text")

        } catch (e: Exception) {

            Log.v(testTag, e.toString())

            Pair(true, "Couldn't click object with text: $text")
        }
    }

    fun changeText(res: String, text: String): Pair<Boolean, String> {
        return try {
            device.wait(Until.hasObject(By.res(res)), timeout)

            val obj = device.findObject(By.res(res))
            obj.text = text

            Pair(true, "Successfully changed text of object: $res with text: $text")

        } catch (e: Exception) {

            Log.v(testTag, e.toString())

            Pair(false, "Failed to change text of object: $res with text: $text")
        }
    }

    fun getNodeTextByRes(res: String): String? {
        return try {

            device.wait(Until.hasObject(By.res(res)), timeout)
            val obj = device.findObject(By.res(res))

            var returnVal = obj.text
            if (returnVal == null) {
                returnVal = ""
            }

            returnVal

        } catch (e: Exception) {

            Log.v(testTag, e.toString())
            null
        }
    }

    fun getNodeByRes(res: String): UiObject2? {
        return try {
            device.wait(Until.hasObject(By.res(res)), timeout)
            device.findObject(By.res(res))

        } catch (e: Exception) {

            Log.v(testTag, e.toString())
            null
        }
    }

    fun screenContainsId(res: String, time: Long = 1000): Boolean {
        return device.wait(Until.hasObject(By.res(res)), time) != null
    }

    fun gestureScrollDown(): Pair<Boolean, String> {
        return try {
            val scrollable = UiScrollable(UiSelector().scrollable(true))
            scrollable.scrollForward()

            Pair(true, "Successfully scrolled down using UiScrollable.")
        } catch (e: Exception) {
            logToFile("Failed to scroll using UiScrollable: ${e.message}")
            Log.e("scrollDown", "Error: ${e.message}")
            Pair(false, "Failed to scroll using UiScrollable.")
        }
    }

    fun findObjectByClassAndInstance(className: String, instance: Int): UiObject? {
        return try {
            val obj = device.findObject(
                UiSelector().className(className).instance(instance)
            )
            obj
        } catch (e: UiObjectNotFoundException) {
            Log.v(testTag, "Could not find object with className: $className and instance: $instance")
            null
        }
    }

}