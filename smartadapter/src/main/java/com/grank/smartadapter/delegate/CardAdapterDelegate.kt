package com.grank.smartadapter.delegate

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

/*
 * -----------------------------------------------------------------
 * description：每个ViewCard显示Adapter的基类
 * 将不同数据结构和显示的ViewHolder做对应关系
 * 扩充不同ViewCard显示模板Adapter，都需要直接过间接继承该类
 * -----------------------------------------------------------------
 */
abstract class CardAdapterDelegate<T, VH : RecyclerView.ViewHolder> {

    var tag = DEFAULT_TAG
    var viewSource: String = ""
    constructor()

    constructor(tag: String) {
        if (tag.isEmpty()) {
            throw NullPointerException("The tag of ${javaClass.name} is null.")
        }
        this.tag = tag
    }

    open fun isForViewType(item: T, position: Int) = true


    abstract fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): VH


    abstract fun onBindViewHolder(holder: VH, position: Int, item: T)


    open fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any>?, item: T) {}


    open fun onViewRecycled(holder: VH?) {}

    open fun onFailedToRecycleView(holder: VH) = false


    open fun onViewAttachedToWindow(holder: VH?) {}


    open fun onViewDetachedFromWindow(holder: VH?) {}

    open fun getMinShowNumber() = 1


    open fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {}


    open fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {}

    open fun setVisibility(boolean: Boolean?) {}

    open fun onPause() {}

    open fun onResume() {}

    open fun onDestroy() {}

    companion object {
        const val DEFAULT_TAG = ""
    }
}
