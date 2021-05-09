package com.grank.uiarch.ui.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.grank.logger.Log

abstract class BaseActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "BaseActivity"
        private val DEBUG = true
    }

    private fun log(msg: String) {
        if (DEBUG) {
            Log.i(TAG, msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!inherit()) {
            throw IllegalStateException("you can not inherit BaseActivity directly, but use AbsDataBindingActivity instead!!")
        }
        super.onCreate(savedInstanceState)
        log(" onCreate $this")
//        window.navigationBarColor = Color.parseColor("#253135")
    }

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

    /**
     * 防止业务实现方直接继承该类
     * @return Boolean
     */
    protected open fun inherit() = false

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        log(" onNewIntent $this")
    }

    override fun onResume() {
        super.onResume()
        log(" onResume $this")
    }

    override fun onPause() {
        super.onPause()
        log(" onPause $this")
    }

    override fun onStop() {
        super.onStop()
        log(" onStop $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        log(" onDestroy $this")
    }
}