package com.grank.uicommon.util

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun Activity.checkAndRequestPermissions(code: Int, permissions: List<String>): Boolean {
    val deniedPermissions = getDeniedPermissions(permissions)
    if (deniedPermissions.isEmpty())
        return true
    ActivityCompat.requestPermissions(this, permissions.toTypedArray(), code)
    return false
}

fun Activity.getDeniedPermissions(permissions: List<String>): List<String> {
    return permissions.filter {
        ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
    }
}

fun Activity.requestPermissions(code: Int, permissions: List<String>) {
    ActivityCompat.requestPermissions(this, permissions.toTypedArray(), code)
}

fun Activity.checkPermissions(permissions: List<String>): Boolean {
    return getDeniedPermissions(permissions).isEmpty()
}