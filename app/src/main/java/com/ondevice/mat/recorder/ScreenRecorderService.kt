package com.ondevice.mat.recorder

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ondevice.mat.R


class ScreenRecorderService : Service() {

    enum class Actions {
        START, STOP
    }

    enum class Constants {
        RESULT_CODE, DATA
    }

    private var handler: Handler? = null
    private var screenRecorder: ScreenRecorder = ScreenRecorder()
    private var imageReader: ImageReader? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var mediaProjection: MediaProjection? = null

    inner class ScreenRecorderBinder : Binder() {
        fun getService(): ScreenRecorderService = this@ScreenRecorderService
    }

    private val binder = ScreenRecorderBinder()

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        Thread {
            Looper.prepare()
            handler = Handler(Looper.myLooper()!!)
            Looper.loop()
        }.start()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            Actions.START.toString() -> startScreenRecording(intent)
            Actions.STOP.toString() -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    fun takeScreenshot(fileName: String) {
        imageReader?.let { screenRecorder.takeScreenshot(it, fileName) }
    }

    @SuppressLint("WrongConstant")
    private fun startScreenRecording(intent: Intent?) {

        if (intent == null) return

        val notification = NotificationCompat.Builder(this, "MATScreenRecorder")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Screen recorder is active")
            .setContentInfo("Recording the screen...")
            .build()

        startForeground(1, notification)

        val resultCode = intent.getIntExtra(Constants.RESULT_CODE.toString(), Activity.RESULT_OK)
//
        val data: Intent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.DATA.toString(), Intent::class.java)
        } else {
            intent.getParcelableExtra(Constants.DATA.toString())
        }

        if (data == null) {
            stopScreenRecording()
            return
        }

        val mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        if(!setup(mediaProjectionManager, resultCode, data)) stopScreenRecording()

    }

    private fun stopScreenRecording() {
        handler?.post(
            Runnable {
                mediaProjection?.stop()
            }
        )
        stopSelf()
    }

    fun setup(mediaProjectionManager: MediaProjectionManager, resultCode: Int, data: Intent): Boolean {
        if (!createMediaProjection(mediaProjectionManager, resultCode, data)) return false

        val density = Resources.getSystem().displayMetrics.density
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels

        if(!createImageReader(width, height)) return false

        if(!createVirtualDisplay(width, height, density.toInt())) return false

        try {
            mediaProjection?.registerCallback(MediaProjectionCallback(), handler)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    inner class MediaProjectionCallback : MediaProjection.Callback() {

        override fun onStop() {
            Log.v("DebugTag", "Stopping media projection")
            handler?.post (
                Runnable {
                    virtualDisplay?.release()
                    imageReader?.close()
                    mediaProjection?.unregisterCallback(this@MediaProjectionCallback)

                }
            )

        }

    }


//    fun close() {
//
//    }


    private fun createMediaProjection(mediaProjectionManager: MediaProjectionManager, resultCode: Int, data: Intent): Boolean {
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)

        return mediaProjection != null

    }

    @SuppressLint("WrongConstant")
    private fun createImageReader(width: Int, height: Int): Boolean {

        try {
            imageReader = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ImageReader.Builder(width, height).build()
            } else {
                ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    private fun createVirtualDisplay(width: Int, height: Int, density: Int): Boolean {
        virtualDisplay = mediaProjection?.createVirtualDisplay(
            "virtualDisplay",
            width,
            height,
            density,
            virtualDisplayFlags(),
            imageReader!!.surface,
            null, handler
        )

        return virtualDisplay != null
    }

    private fun virtualDisplayFlags(): Int {
        return DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC
    }




}