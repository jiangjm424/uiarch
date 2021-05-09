package com.grank.uiarch

import android.app.Application
import com.grank.uiarch.init.InitManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp:Application() {

    @Inject
    lateinit var initManager: InitManager

    override fun onCreate() {
        super.onCreate()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            initManager.init(true, getProcessName())
        } else {
            initManager.init(true, "no_process")
        }
    }
}