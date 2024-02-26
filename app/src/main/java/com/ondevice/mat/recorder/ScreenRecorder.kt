package com.ondevice.mat.recorder

import android.graphics.Bitmap
import android.media.Image
import android.media.ImageReader
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class ScreenRecorder {

    private var folderPath: String = ""
    private val folderName: String = "MAT_screenshots"

    init {

        folderPath = getOutputFolder()

    }

    private fun getOutputFolder(): String {
        val folder = File(Environment.getExternalStorageDirectory().toString() + "/" + folderName)

        val folderCreated = if (!folderExists(folder)) {
            folder.mkdir()
        } else {
            true
        }

        if (folderCreated) {
            return folder.absolutePath
        }

        return ""

    }

    private fun folderExists(folder: File): Boolean {
        return folder.exists() && folder.isDirectory
    }

    fun takeScreenshot(imageReader: ImageReader, fileName: String) {
        try {
            val image: Image? = imageReader.acquireLatestImage()

            if (image != null) {
                val planes = image.planes
                val buffer = planes[0].buffer
                val width = image.width
                val height = image.height

                val bitmap = Bitmap.createBitmap(
                    width, height, Bitmap.Config.ARGB_8888
                )

                bitmap.copyPixelsFromBuffer(buffer)

                // Save the bitmap to a file
                saveBitmapToFile(bitmap, fileName)

                image.close()

                Log.v("DebugTag", "Image saved successfully")
            } else {
                Log.v("DebugTag", "No image available")
            }
        } catch (e: Exception) {
            Log.v("DebugTag", e.toString())
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap, fileName: String) {
        val file = File(folderPath, fileName)

        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        Log.v("DebugTag", "Image saved to: $file")
    }


}