package com.ondevice.mat

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class Adapter(context: Context, availableApps: List<String>) : BaseAdapter() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val TAG: String = "DebugTag"

    private val appList: List<PackageInfo> by lazy {
        val packageManager: PackageManager = context.packageManager
        val packages: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

        packages
            .filter { it.packageName in availableApps }
//            .map { it }
    }

    /**
     * Checks if a package is a system package, we don't want this in our app list because we don't want to test these applications.
     * We only want the applications in the list that need to be tested.
     * source: https://stackoverflow.com/questions/8784505/how-do-i-check-if-an-app-is-a-non-system-app-in-android
     */
    private fun PackageInfo.isSystemPackage(): Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    /**
     * Checks if a package is the package we use to start the tests. We don't want to include this in our application overview
     */
    private fun PackageInfo.isOwnTestApplication(context: Context): Boolean {
        return context.packageName == applicationInfo.packageName
    }

    override fun getCount(): Int {
        return appList.size
    }

    override fun getItem(id: Int): PackageInfo {
        return if (count > id) {
            appList[id]
        } else {
            appList.last()
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View? = layoutInflater.inflate(R.layout.installed_app_list, parent, false)

        val dataItem: PackageInfo = getItem(position)

        // Just set the text
        val textView: TextView = view!!.findViewById(R.id.app_package)
        textView.text = dataItem.packageName

        // Return the populated view
        return view
    }


}