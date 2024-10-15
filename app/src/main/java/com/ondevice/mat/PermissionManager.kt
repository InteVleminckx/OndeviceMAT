package com.ondevice.mat

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ondevice.mat.accessibility.MATAccessibilityService

class PermissionManager(
    private val mainActivity: MainActivity
) {

    private val STORAGE_PERMISSION_CODE = 100

    private var storageAccessGranted: Boolean  = false
    private var screenCaptureGranted: Boolean  = false
    private var accessibilityGranted: Boolean  = false

//    private var screenCaptureData: Intent? = null

    private val storageActivityResultLauncher =
        mainActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (checkExternalStoragePermission()) {
                toast("External Storage Permission Granted!")
                storageAccessGranted = true
            } else {
                toast("External Storage Permission Denied!")
                storageAccessGranted = false
            }
        }

    private val screenCaptureActivityResultLauncher =
        mainActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                toast("Screen Capture Permission Granted!")
                screenCaptureGranted = true
//                screenCaptureData = it.data
            } else {
                toast("Screen Capture Permission Denied!")
                screenCaptureGranted = false
            }
        }

    private val accessibilityServiceActivityResultLauncher =
        mainActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (isAccessibilityServiceEnabled()) {
                toast("Accessibility Service Permission Granted!")
                accessibilityGranted = true
            } else {
                toast("Accessibility Service Permission Denied!")
                accessibilityGranted = false
            }
        }

    fun requestPermissions() {
        if (checkExternalStoragePermission()) {
            toast("External Storage Permission Granted!")
            storageAccessGranted = true
        } else {
            requestExternStoragePermission()
        }

        requestScreenCapturePermission()

        if (isAccessibilityServiceEnabled()) {
            toast("Accessibility Service Permission Granted!")
            accessibilityGranted = true
        } else {
            requestAccessibilityPermission()
        }

    }

    private fun versionCheck(version: Int): Boolean {
        return Build.VERSION.SDK_INT >= version
    }

    private fun checkExternalStoragePermission(): Boolean {

        return if (versionCheck(Build.VERSION_CODES.R)) {
            Environment.isExternalStorageManager()
        } else {
            val write =
                ContextCompat.checkSelfPermission(mainActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read =
                ContextCompat.checkSelfPermission(mainActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestExternStoragePermission() {
        toast("Requesting External Storage Permission!")
        if (versionCheck(Build.VERSION_CODES.R)) {
            // Android is 11 or above
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", mainActivity.packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)

            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }

        } else {
            // below Android 11
            ActivityCompat.requestPermissions(
                mainActivity,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun requestScreenCapturePermission() {
        val mediaProjectionManager =
            mainActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        screenCaptureActivityResultLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
    }


    /**
     * Checks if the MATAccessibilityService is enabled in the device settings.
     *
     * @return True if the service is enabled, false otherwise.
     */
    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityService = ComponentName(mainActivity, MATAccessibilityService::class.java)

        // Retrieve the comma-separated list of enabled accessibility services from settings
        val enabledServicesSetting = Settings.Secure.getString(
            mainActivity.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )

        if (enabledServicesSetting === null) {
            return false
        }

        // Handle empty setting case, this means there are no accessibility services active
        if (enabledServicesSetting.isEmpty()) {
            return false
        }

        // Split the setting string into a list of component names
        val componentNameStringList = TextUtils.SimpleStringSplitter(':').run {
            setString(enabledServicesSetting)
            toList()
        }

        // Convert each string to a ComponentName object and check if it matches the service
        return componentNameStringList
            .mapNotNull { ComponentName.unflattenFromString(it) }
            .any { it == accessibilityService }
    }

    private fun requestAccessibilityPermission() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        accessibilityServiceActivityResultLauncher.launch(intent)
    }

    private fun toast(message: String) {
        Toast.makeText(mainActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun storageGranted(): Boolean {
        return storageAccessGranted || checkExternalStoragePermission()
    }

    fun screenCaptureGranted(): Boolean {
        return screenCaptureGranted
    }

//    fun getScreenCaptureData(): Intent? {
//        return screenCaptureData
//    }

    fun accessibilityServiceGranted(): Boolean {
        return accessibilityGranted
    }

    fun allPermissionsGranted(): Boolean {
        return storageGranted() && accessibilityServiceGranted() && screenCaptureGranted()
//        return storageGranted() && accessibilityServiceGranted()
    }



}