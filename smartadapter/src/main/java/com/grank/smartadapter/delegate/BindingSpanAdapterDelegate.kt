package com.grank.smartadapter.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.grank.smartadapter.vh.BindingViewHolder
import com.grank.smartadapter.SmartCardData


/*
 * -----------------------------------------------------------------
 * description：基于SpanAdapterDelegate 每行显示列数控制的Databinding扩展AdapterDelegate
 * -----------------------------------------------------------------
 */
abstract class BindingSpanAdapterDelegate<T,BD:ViewDataBinding> : BindingAdapterDelegate<T, BD> {

    constructor(tag: String) : super(tag)

    open var spanSize = 1
}
