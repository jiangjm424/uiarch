package com.grank.uiarch.testdi

import android.app.Application
import com.grank.logger.Log
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

class SelfDi @Inject constructor(val app:Application) {
    init {
        Log.i("jiang","self di @this")
    }
    fun pp(){

    }
}