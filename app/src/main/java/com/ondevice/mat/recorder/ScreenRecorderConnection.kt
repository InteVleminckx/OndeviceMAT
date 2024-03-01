package com.ondevice.mat.recorder

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

/**
 * Sets up a connection with the screen recorder service
 * In this way just one service will be created and can be used at multiple places
 */
class ScreenRecorderConnection {

    private var screenRecorderService: ScreenRecorderService? = null

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, screenRecorderBinder: IBinder) {
            val binder = screenRecorderBinder as ScreenRecorderService.ScreenRecorderBinder
            screenRecorderService = binder.getService()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            screenRecorderService = null
        }
    }

    fun isActive() : Boolean {
        return screenRecorderService != null
    }

    fun getConnection(): ServiceConnection {
        return serviceConnection
    }

    fun getScreenRecorderService(): ScreenRecorderService? {
        return screenRecorderService
    }

}