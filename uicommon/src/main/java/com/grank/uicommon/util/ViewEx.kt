package com.grank.uicommon.util

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.databinding.BindingAdapter

const val TO_VISIBLE = 1
const val TO_GONE = 0

/**
 * 透明度变换动画
 */
fun View.alphaAnimation(type: Int) {
    val fromAlpha: Float
    val toAlpha: Float
    when (type) {
        TO_VISIBLE -> {
            this.visibility = View.VISIBLE
            fromAlpha = 0f
            toAlpha = 1f
        }
        else -> {
            this.visibility = View.GONE
            fromAlpha = 1f
            toAlpha = 0f
        }
    }
    val alphaAnimation = AlphaAnimation(fromAlpha, toAlpha)
    alphaAnimation.duration = 300
    this.startAnimation(alphaAnimation)
}

/**
 * 位移动画
 */
fun View.translate(from: Float, to: Float) {
    val translateAnimation = TranslateAnimation(from, to, 0f, 0f)
    translateAnimation.duration = 1000
    translateAnimation.interpolator =
        if (to == 0f) DecelerateInterpolator() else AccelerateInterpolator()
    translateAnimation.fillAfter = true
    this.startAnimation(translateAnimation)
}

/**
 * 方便控件view的显示与隐藏，同时也方便使用其是否显示的属性
 */
@set:BindingAdapter("visible")
var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }
/**
 * 注册防重点击事件
 */
//@SuppressLint("CheckResult")
//fun View.avoidDoubleClick(block: () -> (Unit)) {
//    RxView.clicks(this)
//        .throttleFirst(500, TimeUnit.MILLISECONDS)
//        .subscribe {
//            block.invoke()
//        }
//}