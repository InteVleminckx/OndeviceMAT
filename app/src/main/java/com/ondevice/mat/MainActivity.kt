package com.ondevice.mat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var allowPermission: Button? = null
    private var performInteractions: Button? = null
    private val TAG = "DebugTag"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set the main content view
        setContentView(R.layout.activity_main)

        val appList = findViewById<ListView>(R.id.installed_app_list)

        // Create adapter
        val adapter: Adapter = Adapter(this)
        appList.adapter = adapter

        // Search for the allow permission button
        allowPermission = findViewById(R.id.allowPermission)

        // Search for the perform interactions button
        performInteractions = findViewById(R.id.performInteractions)

        // Check if the button is found or not
        if (allowPermission !== null) {

            // When the button is pressed, open the accessibility settings, where the user
            // can enable the accessibility service
            allowPermission!!.setOnClickListener(View.OnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            })
        }

        // Check if the button is found or not
        if (performInteractions !== null) {

//            // When the button is pressed, call a function that performs some interaction on the mobile
            performInteractions!!.setOnClickListener(View.OnClickListener {
                adapter.printApps()
//                pressHome(this)
//                val pm: PackageManager = packageManager
//                Log.d(TAG, "Start fetching")
//
//                // Source: https://proandroiddev.com/how-to-get-users-installed-apps-in-android-11-b4a4d2754286
//                val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
//                packages
//                    .filterNot { it.isSystemPackage() || it.isMyself() || it.isDMLauncher() }
//                    .map {
//                        it
//                    }
//                Log.d(TAG, "Fetched packages")
//                for (packageInfo in packages) {
//                    Log.d(TAG, "Installed package :" + packageInfo.packageName)
//                    Log.d(TAG, "Source dir : " + packageInfo.sourceDir)
//                    Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName))
//                }
            })

            // find all apps on the screen
//            val appList = findViewById<ListView>(R.id.installed_app_list)


        }
    }

    private fun PackageInfo.isSystemPackage(): Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    private fun pressHome(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)

        val intent2 = Intent(Intent.ACTION_MAIN)
        intent2.addCategory(Intent.CATEGORY_APP_BROWSER)
        intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent2)
    }

}
