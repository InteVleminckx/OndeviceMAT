package com.ondevice.mat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.ondevice.mat.automation.Automator

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set the main content view
        setContentView(R.layout.activity_main)

        val appList = findViewById<ListView>(R.id.installed_app_list)

        // Create adapter
        val adapter = Adapter(this)
        appList.adapter = adapter

        appList.onItemClickListener = AdapterView.OnItemClickListener{_, _, _, l ->
            Automator.targetApk = adapter.getItem(l.toInt()).packageName
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}
