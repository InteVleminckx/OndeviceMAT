package com.ondevice.mat

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.projection.MediaProjectionManager
import android.os.*
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.ondevice.mat.automation.Automator
import com.ondevice.mat.recorder.ScreenRecorderConnection
import com.ondevice.mat.recorder.ScreenRecorderService
import java.io.File


class MainActivity : AppCompatActivity() {


    private val permissionManager = PermissionManager(this)
    private val screenRecorderConnection = ScreenRecorderConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set the main content view
        setContentView(R.layout.activity_main)

        // Requesting external storage
        permissionManager.requestPermissions()

        val appList = findViewById<ListView>(R.id.installed_app_list)

        // Create adapter
        val adapter = Adapter(this)
        appList.adapter = adapter

        appList.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
            startRecording()
            Automator.targetApk = adapter.getItem(id.toInt()).packageName
            Automator.permissionsChecked = permissionManager.allPermissionsGranted()
            Automator.screenRecorderConnection = screenRecorderConnection
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unbind the service when the activity is destroyed
        stopRecording()
    }


    private fun startRecording() {
        if (permissionManager.allPermissionsGranted()) {
            val intent = Intent(this, ScreenRecorderService::class.java)
            intent.action = ScreenRecorderService.Actions.START.toString()
            intent.putExtra(ScreenRecorderService.Constants.DATA.toString(), permissionManager.getScreenCaptureData())
            startService(intent)
            bindService(intent, screenRecorderConnection.getConnection(), Context.BIND_AUTO_CREATE)
        }
    }

    private fun stopRecording() {
        if (screenRecorderConnection.isActive()) {
            unbindService(screenRecorderConnection.getConnection())
        }
        Intent(applicationContext, ScreenRecorderService::class.java).also {
            it.action = ScreenRecorderService.Actions.STOP.toString()
            startService(it)
        }
    }


}
