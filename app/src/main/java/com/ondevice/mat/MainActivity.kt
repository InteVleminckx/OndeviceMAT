package com.ondevice.mat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var allowPermission: Button? = null
    private val TAG = "MyOwnTag"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        allowPermission = findViewById(R.id.allowPermission)
        if (allowPermission !== null) {
            allowPermission!!.setOnClickListener(View.OnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            })
        }
    }
}
