package com.grank.uiarch.init

import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import com.grank.logger.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.jessyan.autosize.AutoAdaptStrategy
import me.jessyan.autosize.AutoSizeConfig
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Singleton
class InitManager
@Inject constructor(
    private val context: Application,
    private val adaptStrategy: AutoAdaptStrategy,
    private val windowManager: WindowManager,
    @Named("app")
    private val appLifecycle: Lifecycle,
    @Named("app")
    private val appLifecycleCoroutineScope: LifecycleCoroutineScope
) {

    fun init(mainProcess: Boolean, processName: String) {
        initBlockSync("logger") {
            initLogger(context, processName)
        }

        initBlockSync("android auto size") {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(metrics)
            val heightPixels = metrics.heightPixels
            val widthPixels = metrics.widthPixels
            AutoSizeConfig.getInstance()
                .setUseDeviceSize(true)
                .setScreenHeight(heightPixels)
                .setExcludeFontScale(true)
                .setScreenWidth(widthPixels)
                .setLog(false)
            context.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    Log.v("onConfigurationChanged, ")
//                    activityStackManager.getCurrentActivity()?.let {
//                        adaptStrategy.applyAdapt(it, it)
//                    }
                }

                override fun onLowMemory() {
                }

            })
        }
        Log.i(TAG, "init done!!")
    }

    private fun initLogger(context: Context, processName: String) {
        Log.initLogger(context, processName, "UiArch", "", true) {
            appLifecycleCoroutineScope.launch {
                withContext(Dispatchers.IO) {
                    Log.trimLog()
                }
            }
        }
    }

    private inline fun initBlockSync(label: String, condition: Boolean = true, block: () -> Unit) {
        if (condition) {
            val costTime = measureTimeMillis(block)
            Log.d(TAG, "init $label cost ${costTime}ms")
        } else {
            Log.d(TAG, "init $label not execute")
        }
    }

    companion object {
        private const val TAG = "InitManager"
    }
}