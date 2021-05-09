package com.grank.netcore.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import com.grank.logger.Log

interface PropFetcher {
    fun getIMEI(): String?
    fun getQKey(): String?
}


@SuppressLint("MissingPermission", "HardwareIds", "NewApi")
class VendorPlatform(private val context: Context, propFetcher: PropFetcher? = null) {


    val androidId: String by lazy {
        try {
            Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            Log.d("get exception", e)
            ""
        }
    }

    private fun getCachedId(key: String): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null)
    }

    private fun saveCachedId(key: String, id: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().putString(key, id).apply()
    }

    val imei: String? by lazy {
        val cacheKey = "CACHE_IM"
        val cachedImei = getCachedId(cacheKey)
        if (!TextUtils.isEmpty(cachedImei))
            return@lazy cachedImei!!
        val propFetchImei = propFetcher?.getIMEI()
        if (!propFetchImei.isNullOrEmpty()) {
            saveCachedId(cacheKey, propFetchImei)
            return@lazy propFetchImei
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return@lazy "AID_$androidId"
        }
        //Android Q限制了IMEI获取，这样获取会抛出异常
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val imei =
                    (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).getImei(
                        0
                    )
                if (!imei.isNullOrEmpty()) {
                    saveCachedId(cacheKey, imei)
                    return@lazy imei
                }
            }
        } catch (e: Exception) {
            Log.e("get something error")
//                Log.e("error", e)
        }
        null
    }

    val imsi: String? by lazy {
        val cacheKey = "CACHE_IS"
        val cachedImsi = getCachedId(cacheKey)
        if (!TextUtils.isEmpty(cachedImsi))
            return@lazy cachedImsi!!
        //Android Q限制了IMSI获取，这样获取会抛出异常
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val tmp =
                    (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).subscriberId
                if (!tmp.isNullOrEmpty()) {
                    saveCachedId(cacheKey, tmp)
                    return@lazy tmp
                }
            }
        } catch (e: Exception) {
            Log.e("get something error")
//                Log.e("error", e)
        }
        null
    }

    val appPackage: String by lazy {
        context.packageName
    }

    private val appPackageInfo: PackageInfo by lazy {
        context.packageManager.getPackageInfo(context.packageName, 0)
    }

    val appVersionCode: Int by lazy {
        appPackageInfo.versionCode
    }

    val appVersionName: String by lazy {
        appPackageInfo.versionName
    }

    val romVersion: String? by lazy {
        val version = System.getProperty("ro.build.asus.version", null)
        version
    }

    val qKey: String by lazy {
        val tmp = System.getProperty("ro.tc.qkey", null)
        if (!tmp.isNullOrEmpty()) {
            return@lazy tmp
        }
        val defaultQkey = "Ujg4xjmnNqDFiQoC9gKrkm9HsT7bDRa5"

        val propFetchQKey = propFetcher?.getQKey()
        if (!propFetchQKey.isNullOrEmpty()) {
            return@lazy propFetchQKey
        } else {
            return@lazy defaultQkey
        }

    }

    var requestSequence: Int = 0
        get() {
            return ++field
        }
}