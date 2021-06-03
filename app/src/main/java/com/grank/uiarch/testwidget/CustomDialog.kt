package com.grank.uiarch.testwidget

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.grank.uiarch.R
import com.grank.uiarch.databinding.CustomDialogBinding
import com.grank.uicommon.ui.base.BaseDialog
import com.grank.uicommon.ui.base.DialogBuildParam
import com.grank.uicommon.util.dip2px
import com.grank.uicommon.util.visible

open class CustomDialog constructor(
    context: Context,
    private val layoutId: Int = R.layout.custom_dialog,
    dialogBuildParamFactory: ((context: Context) -> DialogBuildParam) = { ctx ->
        DialogBuildParam(
            width = ctx.dip2px(349),
            paddingBottom = ctx.dip2px(24)
        )
    }
) : BaseDialog(
    context,
    themeResId = R.style.CustomDialog,
    dialogBuildParamFactory = dialogBuildParamFactory
) {

    private lateinit var rootView: CustomDialogBinding
    private val customDialogBuildParam = CustomDialogBuildParam()

    private var mCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        rootView = CustomDialogBinding.inflate(layoutInflater)
        initRootView()
        setContentView(rootView.root)

        mCreated = true
    }

    private fun initRootView() {
        rootView.content.highlightColor = Color.TRANSPARENT
        dialogBuildParam.dialogBackground?.let {
            rootView.root.background = it
        }

        if (customDialogBuildParam.titleResId != -1) {
            rootView.title.setText(customDialogBuildParam.titleResId)
        }
        customDialogBuildParam.titleText?.let { rootView.title.text = it }

        if (customDialogBuildParam.subTitleResId != -1) {
            rootView.subTitle.setText(customDialogBuildParam.subTitleResId)
        }
        customDialogBuildParam.subTitleText?.let { rootView.subTitle.text = it }

        customDialogBuildParam.contentStr?.let {
            rootView.content.text = it
            if (customDialogBuildParam.containUrl) {
                rootView.content.movementMethod = LinkMovementMethod.getInstance()
            }
        }

        if (customDialogBuildParam.customLayoutRes != -1) {
            rootView.content.visibility = View.GONE
            rootView.customView.visibility = View.VISIBLE
            rootView.customView.removeAllViews()
            LayoutInflater.from(context).inflate(customDialogBuildParam.customLayoutRes, rootView.customView, true)
        } else if (customDialogBuildParam.customView != null) {
            rootView.content.visibility = View.GONE
            rootView.customView.visibility = View.VISIBLE
            rootView.customView.removeAllViews()
            rootView.customView.addView(customDialogBuildParam.customView)
        }

        if (customDialogBuildParam.contentResId != -1) {
            rootView.content.setText(customDialogBuildParam.contentResId)
        }
        rootView.title.visibility =
            if (customDialogBuildParam.showTitle)
                View.VISIBLE
            else
                View.GONE

        if (customDialogBuildParam.showCancelButton) {
            rootView.cancel.visibility = View.VISIBLE
            rootView.vLineVertical.visibility =
                if (rootView.confirm.visibility == View.VISIBLE) View.VISIBLE else View.GONE
        } else {
            rootView.cancel.visibility = View.GONE
            rootView.vLineVertical.visibility = View.GONE
        }

        if (customDialogBuildParam.showConfirmButton) {
            rootView.confirm.visibility = View.VISIBLE
            rootView.vLineVertical.visibility =
                if (rootView.cancel.visibility == View.VISIBLE) View.VISIBLE else View.GONE
        } else {
            rootView.confirm.visibility = View.GONE
            rootView.vLineVertical.visibility = View.GONE
        }

        if (customDialogBuildParam.cancelButtonResId != -1) {
            rootView.cancel.setText(customDialogBuildParam.cancelButtonResId)
        }
        if (customDialogBuildParam.confirmButtonResId != -1) {
            rootView.confirm.setText(customDialogBuildParam.confirmButtonResId)
        }
        customDialogBuildParam.cancelButtonText?.let { rootView.cancel.text = it }
        customDialogBuildParam.confirmButtonText?.let { rootView.confirm.text = it }

        rootView.confirm.setOnClickListener {
            customDialogBuildParam.clickListener?.onClick(this,DialogInterface.BUTTON_POSITIVE)
            dismiss()
        }
        rootView.cancel.setOnClickListener {
            customDialogBuildParam.clickListener?.onClick(this,DialogInterface.BUTTON_NEGATIVE)
            dismiss()
        }
    }

    /****** 静态设置弹窗内容 START ******/
    override fun setTitle(titleId: Int) {
        super.setTitle(titleId)
        customDialogBuildParam.titleResId = titleId
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        customDialogBuildParam.titleText = title
    }

    fun setSubTitle(subTitleId: Int) {
        customDialogBuildParam.titleResId = subTitleId
    }

    fun setSubTitle(subTitle: CharSequence?) {
        customDialogBuildParam.subTitleText = subTitle
    }

    fun setContent(contentStr: CharSequence?, containUrl: Boolean = false) {
        customDialogBuildParam.contentStr = contentStr
        customDialogBuildParam.containUrl = containUrl
    }

    fun setContent(@StringRes resId: Int) {
        customDialogBuildParam.contentResId = resId
    }

    fun setCustomView(@LayoutRes layoutRes: Int) {
        customDialogBuildParam.customLayoutRes = layoutRes
    }

    fun setCustomView(view: View) {
        customDialogBuildParam.customView = view
    }

    fun showTitle(visible: Boolean) {
        customDialogBuildParam.showTitle = visible
    }

    fun showCancelButton(visible: Boolean) {
        customDialogBuildParam.showCancelButton = visible
    }

    fun showConfirmButton(visible: Boolean) {
        customDialogBuildParam.showConfirmButton = visible
    }

    fun setCancelButtonText(text: CharSequence) {
        customDialogBuildParam.cancelButtonText = text
    }

    fun setConfirmButtonText(text: CharSequence) {
        customDialogBuildParam.confirmButtonText = text
    }

    fun setCancelButtonText(@StringRes resId: Int) {
        customDialogBuildParam.cancelButtonResId = resId
    }

    fun setConfirmButtonText(@StringRes resId: Int) {
        customDialogBuildParam.confirmButtonResId = resId
    }

    fun setClickListener(listener: DialogInterface.OnClickListener) {
        customDialogBuildParam.clickListener = listener
    }
    /****** 静态设置弹窗内容 END ******/


    /****** 动态改变弹窗内容 START  ******/
    fun title(titleStr: String) {
        if (!isCreated()) {
            customDialogBuildParam.titleText = titleStr
            return
        }
        rootView.title.visibility =
            if (titleStr.isEmpty())
                View.GONE
            else
                View.VISIBLE
        rootView.title.text = titleStr
    }

    fun subTitle(subTitle: String) {
        if (!isCreated()) {
            customDialogBuildParam.subTitleText = subTitle
            return
        }
        rootView.subTitle.visibility =
            if (subTitle.isEmpty())
                View.GONE
            else
                View.VISIBLE
        rootView.subTitle.text = subTitle
    }

    fun subTitleColor(color: Int) {
        if (!isCreated()) {
            return
        }
        rootView.subTitle.setTextColor(color)
    }

    fun content(contentStr: CharSequence?) {
        rootView.content.visibility = View.VISIBLE
        rootView.content.text = contentStr
        rootView.customView.visibility = View.GONE
    }

    fun contentColor(color: Int) {
        rootView.content.setTextColor(color)
    }

    fun customView(@LayoutRes layout: Int) {
        rootView.content.visibility = View.GONE
        rootView.customView.visibility = View.VISIBLE
        rootView.customView.removeAllViews()
        layoutInflater.inflate(layout, rootView.customView, true)
    }

    fun customView(layout: View) {
        rootView.content.visibility = View.GONE
        rootView.customView.visibility = View.VISIBLE
        rootView.customView.removeAllViews()
        rootView.customView.addView(layout)
    }

    fun showCustomView(visible: Boolean) {
        rootView.customView.visible = visible
        rootView.content.visible = !visible
    }

    fun showCancelText(cancelStr: String) {
        rootView.cancel.visibility =
            if (cancelStr.isNotEmpty())
                View.VISIBLE
            else
                View.GONE
        rootView.cancel.text = cancelStr
    }

    fun showVLine(visible: Boolean) {
        rootView.vLineVertical.visible = visible
    }

    fun showVLineColor(color: Int) {
        rootView.vLineVertical.setBackgroundColor(color)
    }

    fun showHLine(visible: Boolean) {
        rootView.vLineHorizontal.visible = visible
    }

    fun showHLineColor(color: Int) {
        rootView.vLineHorizontal.setBackgroundColor(color)
    }

    fun cancelBtnColor(color: Int) {
        rootView.cancel.setTextColor(color)
    }

    fun showConfirmText(confirmStr: String) {
        rootView.confirm.visibility =
            if (confirmStr.isNotEmpty())
                View.VISIBLE
            else
                View.GONE
        rootView.confirm.text = confirmStr
    }

    fun confirmBtnColor(color: Int) {
        rootView.confirm.setTextColor(color)
    }

    fun clickListener(listener: DialogInterface.OnClickListener?) {
        customDialogBuildParam.clickListener = listener
    }

    fun rootBackground(drawable: Drawable) {
        rootView.root.background = drawable
    }

    /****** 动态改变弹窗内容 END  ******/

    private class CustomDialogBuildParam {
        var titleResId = -1
        var subTitleResId = -1
        var titleText: CharSequence? = null
        var subTitleText: CharSequence? = null
        var contentStr: CharSequence? = null
        var containUrl: Boolean = false
        var contentResId = -1
        var showTitle = true
        var showCancelButton = false
        var showConfirmButton = true
        var cancelButtonText: CharSequence? = null
        var confirmButtonText: CharSequence? = null
        var cancelButtonResId = -1
        var confirmButtonResId = -1
        var customLayoutRes = -1
        var customView: View? = null
        var clickListener: DialogInterface.OnClickListener? = null
    }

    fun isCreated(): Boolean = mCreated

}