package com.grank.uicommon.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.Process
import android.text.TextUtils
import android.view.ViewConfiguration
import android.view.WindowManager
import com.grank.logger.Log

fun Context.getCurrentProcessName(): String {
    var processName: String? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        processName = Application.getProcessName()
    } else {
        val pid = Process.myPid()
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in am.runningAppProcesses) {
            if (appProcess.pid == pid) {
                processName = appProcess.processName
                break
            }
        }
    }
    return processName ?: ""
}

/**
 * dp 转 px
 */
fun Context.dip2px(dpValue: Float): Int {
    val scale = this.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Context.dip2px(dpValue: Int): Int {
    val scale = this.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}


/**
 * 获取屏幕尺寸
 *
 * @param context 上下文
 * @return 屏幕尺寸像素值，下标为0的值为宽，下标为1的值为高
 */
fun Context.getScreenSizeF(): Point {
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val screenSize = Point()
    wm.defaultDisplay.getSize(screenSize)
    return screenSize
}

/**
 * 获取屏幕宽度
 *
 * @param context
 * @return 屏幕宽度
 */
fun Context.getDisplayWidth(): Int {
    val wm =
            this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return wm.defaultDisplay.width
}

/**
 * 获取屏幕高度，不含导航栏
 *
 * @param context
 * @return 屏幕高度
 */
fun Context.getDisplayHeight(): Int {
    val wm =
            this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return wm.defaultDisplay.height
}

/**
 * 获取屏幕高度，增加导航栏高度
 *
 * @param context
 * @return 屏幕高度，含有导航栏
 */
fun Context.getDisplayHeightNew(): Int {
    val wm =
            this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    var height = wm.defaultDisplay.height
    if (this.hasNavBar()) {
        height += this.getNavigationBarHeight()
    }
    return height
}

/**
 * 检查是否有导航栏
 *
 * @param context
 * @return 是否有导航栏
 */
fun Context.hasNavBar(): Boolean {
    val res = this.resources
    val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
    return if (resourceId != 0) {
        var hasNav = res.getBoolean(resourceId)
        // check override flag
        val sNavBarOverride: String? = getNavBarOverride()
        if ("1" == sNavBarOverride) {//没有虚拟按钮
            hasNav = false
        } else if ("0" == sNavBarOverride) {//有虚拟按钮
            hasNav = true
        }
        hasNav
    } else { // fallback
        !ViewConfiguration.get(this).hasPermanentMenuKey()
    }
}

/**
 * 导航栏设置参数
 *
 * @param
 * @return 获取导航栏设置参数
 */
private fun getNavBarOverride(): String? {
    var sNavBarOverride: String? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        try {
            val c = Class.forName("android.os.SystemProperties")
            val m = c.getDeclaredMethod("get", String::class.java)
            m.isAccessible = true
            sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
        } catch (e: Throwable) {
        }
    }
    return sNavBarOverride
}

/**
 * 获取屏幕底部导航菜单栏高度
 *
 * @param context
 * @return 导航栏高度
 */
fun Context.getNavigationBarHeight(): Int {
    val resources = this.resources
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    var navBarHeight = 0
    if (resourceId > 0) {
        navBarHeight = resources.getDimensionPixelSize(resourceId)
    }
    return navBarHeight
}

/**
 *  获取状态栏高度
 */
fun Context.getStatusBarHeight(): Int {
    var height = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        height = resources.getDimensionPixelSize(resourceId)
    }
    return height
}

fun Context.isPackageInstalled(packageName: String): Boolean {
    if (TextUtils.isEmpty(packageName)) {
        return false
    }
    return try {
        val info = packageManager.getApplicationInfo(packageName, 0)
        info != null
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

//通过包名启动一个应用
fun Context.startApp(pkgName: String) {
    val launchIntentForPackage = packageManager.getLaunchIntentForPackage(pkgName)
    if (launchIntentForPackage != null) {
        try {
            startActivity(launchIntentForPackage)
        } catch (t: Throwable) {
            t.printStackTrace()
            Log.w("startApp $pkgName error", t)
            // ToastUtils.showShortToastSafe("打开失败")
        }
    } else {
        Log.w("startApp $pkgName error, no launchIntent")
        //Toast.makeText(context, "打开失败", Toast.LENGTH_SHORT).show()
        //ToastUtils.showShortToastSafe("打开失败")
    }
}

/**
 * 通过包名获取 app名
 * @receiver Context
 * @param pkgName String?
 * @return String?
 */
fun Context.getAppName(pkgName: String?): String? {
    return try {
        val packageInfo = pkgName?.let { packageManager.getPackageInfo(it, 0) }
        packageInfo?.applicationInfo?.loadLabel(packageManager).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


/**
 * 获取指定包名应用的版本号,versionCode
 *
 */
fun Context.getVersionCode(pkgName: String?)//获取版本号(内部识别号)
        : Int {
//        return InstalledPackageRepository.get().installedPackage[pkgName] ?: 0
    return try {
        val pi = pkgName?.let { packageManager.getPackageInfo(it, 0) }
        pi?.versionCode ?: 0
    } catch (e: PackageManager.NameNotFoundException) {
        // TODO Auto-generated catch block
        //e.printStackTrace();
        Log.d("pkg $pkgName not exist")
        0
    }
}

/**
 * 获取指定包名应用的版本名,versionName
 *
 */
fun Context.getVersionName(pkgName: String?)//获取版本号(内部识别号)
        : String {
    return try {
        val pi = pkgName?.let { packageManager.getPackageInfo(it, 0) }
        pi?.versionName ?: ""
    } catch (e: PackageManager.NameNotFoundException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
        ""
    }
}

/**
 *
 * 判断是否有Launcher Icon
 */
fun Context.hasLauncherIcon(pkgName: String?): Boolean {
    if (pkgName == null) return false
    return packageManager.getLaunchIntentForPackage(pkgName) != null
}

fun Context.getPackageInfo(packageName: String): PackageInfo? {
    return try {
        packageManager.getPackageInfo(packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}
