package com.grank.uiarch.testwidget

import android.content.Context
import android.os.Bundle
import com.grank.uiarch.R
import com.grank.uiarch.databinding.CustomDialogBinding
import com.grank.uicommon.ui.base.BaseDialog

class CustomDialog2(context: Context) : BaseDialog(context, R.style.CustomDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = CustomDialogBinding.inflate(layoutInflater)
        root.cancel.setOnClickListener {
            listener?.invoke(0)
        }
        root.confirm.setOnClickListener {
            listener?.invoke(1)
        }
        setContentView(root.root)
    }
    private var listener: ((Int) -> Unit)? = null
    fun setOnClickListener(listener: ((Int) -> Unit)?) {
        this.listener = listener
    }
}