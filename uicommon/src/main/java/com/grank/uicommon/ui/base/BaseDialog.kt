package com.grank.uicommon.ui.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.StyleRes
import me.jessyan.autosize.AutoSizeCompat

class DialogBuildParam(
    val gravity: Int = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
    val width: Int = MATCH_PARENT,
    val height: Int = WRAP_CONTENT,
    val paddingLeft: Int = 0,
    val paddingRight: Int = 0,
    val paddingTop: Int = 0,
    val paddingBottom: Int = 0,
    val fullScreen: Boolean = false,
    val windowAnimator: Int?=null,
    val dialogBackground: Drawable? = null
)

abstract class BaseDialog(
    context: Context,
    @StyleRes themeResId: Int,
    dialogBuildParamFactory: ((context: Context) -> DialogBuildParam)? = null
) :
    Dialog(context, themeResId), DialogInterface {

    protected val dialogBuildParam by lazy {
        dialogBuildParamFactory?.invoke(getContext()) ?: DialogBuildParam()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val resources = context.resources
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            AutoSizeCompat.autoConvertDensityBaseOnHeight(resources, 375f)
        } else {
            AutoSizeCompat.autoConvertDensityBaseOnWidth(resources, 375f)
        }
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(view: View) {
        setWindowStyle()
        super.setContentView(view)
    }

    override fun setContentView(layoutResID: Int) {
        setWindowStyle()
        super.setContentView(layoutResID)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        setWindowStyle()
        super.setContentView(view, params)
    }

    private fun setWindowStyle() {
        window?.setGravity(dialogBuildParam.gravity)
        window?.decorView?.setPadding(
            dialogBuildParam.paddingLeft,
            dialogBuildParam.paddingTop,
            dialogBuildParam.paddingRight,
            dialogBuildParam.paddingBottom
        )
        if (dialogBuildParam.fullScreen) {
            window?.decorView?.let {
//                it.systemUiVisibility = StatusBarHelper.getFullscreenSystemUIVisibility()
            }
        }
        dialogBuildParam.windowAnimator?.let {
            window?.setWindowAnimations(it)
        }

        val params = window?.attributes
        params?.width = dialogBuildParam.width
        params?.height = dialogBuildParam.height
        window?.attributes = params
    }
}