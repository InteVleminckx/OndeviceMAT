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

        // set the main content view
        setContentView(R.layout.activity_main)

        // Search for the allow permission button
        allowPermission = findViewById(R.id.allowPermission)

        // Check if the button is found or not
        if (allowPermission !== null) {

            // When the button is pressed, open the accessibility settings, where the user
            // can enable the accessibility service
            allowPermission!!.setOnClickListener(View.OnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            })
        }
    }
}
