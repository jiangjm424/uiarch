package com.grank.smartadapter.delegate

import androidx.recyclerview.widget.RecyclerView


/*
 * -----------------------------------------------------------------
 * description：扩展模板Adapter 一行只显示一列数据
 * -----------------------------------------------------------------
 */

abstract class SpanAdapterDelegate<T, VH : RecyclerView.ViewHolder> :
    CardAdapterDelegate<T, VH> {

    open var spanSize: Int = 1


    constructor(tag: String) : super(tag)

}
