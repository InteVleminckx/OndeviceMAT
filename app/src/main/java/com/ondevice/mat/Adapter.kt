package com.ondevice.mat

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class Adapter(context: Context) : BaseAdapter() {

    val layoutInflater = LayoutInflater.from(context)

    private val TAG: String = "DebugTag"
    private val appList: List<PackageInfo> by lazy {
        val packageManager: PackageManager = context.packageManager
        val packages: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        packages.map { it }
    }

    fun printApps() {
        for (packageInfo in appList) {
            Log.v(TAG, packageInfo.packageName)
        }
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