package com.grank.routercontroller

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.alibaba.android.arouter.launcher.ARouter

class InitRouterProvider: ContentProvider() {
    override fun onCreate(): Boolean {
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(context as Application?)
        return true
    }

    override fun query(
        uri: Uri, strings: Array<String?>?, s: String?, strings1: Array<String?>?,
        s1: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String?>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String?>?
    ): Int {
        return 0
    }
}