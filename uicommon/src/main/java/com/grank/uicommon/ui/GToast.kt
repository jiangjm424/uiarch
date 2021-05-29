package com.grank.uicommon.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.grank.uicommon.util.dip2px

@SuppressLint("InflateParams")
class GToast(private val context: Context) {
    private val defaultGravity = Triple(Gravity.BOTTOM, 0, context.dip2px(80f))

    private val toast: Toast by lazy {
        Toast(context).apply {
//            val view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null, false)
//            setView(view)
            setGravity(
                defaultGravity.first,
                defaultGravity.second,
                defaultGravity.third
            )
        }
    }

    fun show(charSequence: CharSequence, gravity: Triple<Int, Int, Int> = defaultGravity) {
        show(charSequence, Toast.LENGTH_SHORT, gravity)
    }

    fun show(charSequence: CharSequence, duration: Int, gravity: Triple<Int, Int, Int> = defaultGravity) {
//        (toast.view as TextView).text = charSequence
        toast.setText(charSequence)
        toast.duration = duration
        toast.setGravity(gravity.first, gravity.second, gravity.third)
        toast.show()
    }

    fun show(resId: Int, gravity: Triple<Int, Int, Int> = defaultGravity) {
        show(resId, Toast.LENGTH_SHORT, gravity)
    }

    fun show(resId: Int, duration: Int, gravity: Triple<Int, Int, Int> = defaultGravity) {
//        (toast.view as TextView).setText(resId)
        toast.setText(resId)
        toast.duration = duration
        toast.setGravity(gravity.first, gravity.second, gravity.third)
        toast.show()
    }
}