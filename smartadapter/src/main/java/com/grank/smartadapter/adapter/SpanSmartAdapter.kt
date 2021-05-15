package com.grank.smartadapter.adapter

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grank.smartadapter.delegate.SpanAdapterDelegate


/*
 * -----------------------------------------------------------------
 * description：Span样式布局BaseAdapter
 * 一个行显示数据按照模板SpanAdapterDelegate中定义的spanSize决定
 * -----------------------------------------------------------------
 */

class SpanSmartAdapter(lifecycleOwner: LifecycleOwner) : SmartAdapter(lifecycleOwner) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {

            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val delegate = adapterManager.getDelegate(getItemViewType(position))
                    return if (null != delegate
                            && delegate is SpanAdapterDelegate<Any, out RecyclerView.ViewHolder>
                    ) {
                        delegate.spanSize
                    } else {
                        layoutManager.spanCount
                    }
                }
            }
        }
    }
}
