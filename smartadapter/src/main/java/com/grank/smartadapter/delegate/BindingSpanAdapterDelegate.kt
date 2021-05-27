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
abstract class BindingSpanAdapterDelegate<T> : SpanAdapterDelegate<T, BindingViewHolder> {

    /**
     * 获得模板LayoutRes ID
     */
    @get:LayoutRes
    abstract val layoutRes: Int


    constructor(tag: String) : super(tag)

    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): BindingViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                layoutRes,
                parent,
                false)
        val holder = BindingViewHolder(binding.root)
        holder.setBinding(binding)
        return holder
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int, item: T) {
//        super.onBindViewHolder(holder, position, item)
        if (item is SmartCardData) {
            @Suppress("UNCHECKED_CAST")
            setVariable(holder.getBinding(), item.data as T, position)
        } else {
            setVariable(holder.getBinding(), item, position)
        }
        holder.getBinding<ViewDataBinding>().executePendingBindings()
    }

    /**
     * 更新数据
     */
    abstract fun setVariable(binding: ViewDataBinding, item: T, position: Int)
}
