package com.grank.smartadapter.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.grank.smartadapter.SmartCardData
import com.grank.smartadapter.vh.BindingViewHolder


/*
 * -----------------------------------------------------------------
 * description：为DataBinding 扩展的Adapter 方便子类继承实现不同DataBinding的
 * AdapterDelegate的扩展
 * -----------------------------------------------------------------
 */

abstract class BindingAdapterDelegate<T> : CardAdapterDelegate<T, BindingViewHolder> {

    constructor()

    constructor(tag: String) : super(tag)

    interface ICardCallback {
        fun onCardShow(
            cardType: String?,
            cardSource: String?,
            cardId: String?,
            cardPosition: String?,
            cardName: String?
        )
    }

    /**
     * 获得模板LayoutRes ID
     */
    @get:LayoutRes
    abstract val layoutRes: Int

    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): BindingViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            false
        )
        val holder = BindingViewHolder(binding.root)
        holder.setBinding(binding)
        configureViewHolder(holder, lifecycleOwner)
        return holder
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int, item: T) {
        if (item is SmartCardData) {
            @Suppress("UNCHECKED_CAST")
            // 透传上报字段
            holder.cardId = item.cardId
            holder.cardTitle = item.cardTitle
            setVariable(holder.getBinding(), item.data as T, position)
        } else {
            setVariable(holder.getBinding(), item, position)
        }
        holder.getBinding<ViewDataBinding>().executePendingBindings()
    }

    override fun onViewAttachedToWindow(holder: BindingViewHolder?) {
        super.onViewAttachedToWindow(holder)
        if (viewSource.isEmpty() && holder?.itemView?.context is AppCompatActivity) {
            var source = (holder.itemView.context as AppCompatActivity).localClassName
            val index = source.lastIndexOf(".")
            if (index != -1) {
                source = source.substring(index + 1)
            }
            cardShowed(
                tag,
                source,
                holder.cardId,
                holder.adapterPosition.toString(),
                holder.cardTitle
            )
        } else {
            cardShowed(
                tag,
                viewSource,
                holder?.cardId!!,
                holder.adapterPosition.toString(),
                holder.cardTitle
            )
        }
    }

    open fun cardShowed(
        cardType: String?,
        cardSource: String?,
        cardId: String?,
        cardPosition: String?,
        cardName: String?
    ) {

    }


    /**
     * 设置ViewHolder
     */
    open fun configureViewHolder(holder: BindingViewHolder, lifecycleOwner: LifecycleOwner) {}

    /**
     * 更新数据
     */
    abstract fun setVariable(binding: ViewDataBinding, item: T, position: Int)
}
