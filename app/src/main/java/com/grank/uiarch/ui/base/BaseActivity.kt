package com.grank.uiarch.ui.base

import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewTreeLifecycleOwner

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "BaseActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = Color.parseColor("#253135")
        onCreate2(savedInstanceState)
    }

    abstract fun onCreate2(savedInstanceState: Bundle?)

    override fun setContentView(layoutResID: Int) {
        ViewTreeLifecycleOwner.set(window.decorView, this);
        super.setContentView(layoutResID)
        if (!needCustomStatusBar()) {
            try {//一般情况下不会出现异常，防止万一
//                StatusBarHelper.translucent(this)
//                StatusBarHelper.setStatusBarDarkMode(this)
                window.decorView.findViewById<FrameLayout>(android.R.id.content)[0].fitsSystemWindows =
                    needFitSystemWindow()
            } catch (e: Exception) {
                // 、 Log.e(TAG, "error", e)
            }
        }
    }

    open fun needFitSystemWindow() = true


    /**
     * 状态栏交给activity自己处理
     */
    open fun needCustomStatusBar() = false

}