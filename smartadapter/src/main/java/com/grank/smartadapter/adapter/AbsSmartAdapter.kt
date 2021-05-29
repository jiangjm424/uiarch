package com.grank.smartadapter.adapter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.grank.smartadapter.delegate.AdapterManager
import com.grank.smartadapter.delegate.CardAdapterDelegate

/*
 * -----------------------------------------------------------------
 * description：SmartList Adapter 抽象
 * 主要将Adapter业务逻辑托管给SmartAdapterManager处理
 * 主要继承 RecyclerView.Adapter<RecyclerView.ViewHolder>()
 * 重写重要方法，并将业务逻辑托管给AdapterManager处理
 * -----------------------------------------------------------------
 */
abstract class AbsSmartAdapter constructor(
    val lifecycleOwner: LifecycleOwner,
    protected var adapterManager: AdapterManager = AdapterManager()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    @JvmOverloads
    fun addDelegate(delegate: CardAdapterDelegate<*, *>, cardType: String) {
        delegate.cardType = cardType
        adapterManager.addDelegate(delegate, cardType)
    }

    fun setFallbackDelegate(delegate: CardAdapterDelegate<*, *>) {
        @Suppress("UNCHECKED_CAST")
        adapterManager.backDelegate = delegate as CardAdapterDelegate<Any, RecyclerView.ViewHolder>?
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapterManager.onCreateViewHolder(lifecycleOwner, parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapterManager.onBindViewHolder(holder, position, getItem(position))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, position)
        adapterManager.onBindViewHolder(holder, position, payloads, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return adapterManager.getItemViewType(getItem(position), position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        adapterManager.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return adapterManager.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        adapterManager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        adapterManager.onViewDetachedFromWindow(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        adapterManager.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        adapterManager.onDetachedFromRecyclerView(recyclerView)
    }

    /**
     * 用于判断ViewCard的View类型
     */
    abstract fun getItem(position: Int): Any

    /**
     * 同步Fragment 显示状态，当前Fragment不在显示时，需要同步给卡片知道
     */
    open fun setVisibility(boolean: Boolean) {
        adapterManager.setVisibility(boolean)
    }

    open fun onPause() {
        adapterManager.onPause()
    }

    open fun onResume() {
        adapterManager.onResume()
    }

    open fun onDestroy() {
        adapterManager.onDestroy()
    }

    open fun setViewSource(source: String) {
        adapterManager.setViewSource(source)
    }
}
