package com.grank.uiarch.activity

import android.content.DialogInterface
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
import com.grank.uiarch.R
import com.grank.uiarch.databinding.LayoutActivitySplashBinding
import com.grank.uiarch.testdi.HiltTest
import com.grank.uiarch.testwidget.CustomDialog
import com.grank.uiarch.testwidget.CustomDialog2
import com.grank.uicommon.ui.base.AbsDataBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity: AbsDataBindingActivity<LayoutActivitySplashBinding>() {

    @Inject
    lateinit var hiltTest: HiltTest

    override val layoutRes: Int
        get() = R.layout.layout_activity_splash

    override fun setupView(binding: LayoutActivitySplashBinding) {
        lifecycleScope.launchWhenResumed {
            CustomDialog(this@SplashActivity).apply {
                setClickListener { dialog, which ->
                    when(which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            ARouter.getInstance().build("/app/main").navigation()
                        }
                    }
                }
            }.show()
        }
    }

    override fun setupData(binding: LayoutActivitySplashBinding, lifecycleOwner: LifecycleOwner) {
    }
}