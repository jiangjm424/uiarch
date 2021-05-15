
package com.grank.smartadapter.delegate

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.grank.smartadapter.SmartCardData

/*
 * -----------------------------------------------------------------
 * description：扩展AdapterDelegate 处理点击事件 提供监听点击事件接口，
 * -----------------------------------------------------------------
 */
abstract class ClickableAdapterDelegate<T, VH : RecyclerView.ViewHolder> :
    CardAdapterDelegate<T, VH> {

    constructor()

    constructor(tag: String) : super(tag)

    override fun onBindViewHolder(holder: VH, position: Int, item: T) {
        holder.itemView.setOnClickListener { v ->
            val currentPosition = getPosition(holder)
            // If the item can click
            if (clickable(currentPosition)) {
                if (item is SmartCardData) {
                    @Suppress("UNCHECKED_CAST")
                    onItemClick(v, item.data as T, currentPosition)
                } else {
                    onItemClick(v, item, currentPosition)
                }
            }
        }

        holder.itemView.setOnLongClickListener(View.OnLongClickListener { v ->
            val currentPosition = getPosition(holder)
            // If the item can long click
            if (longClickable(currentPosition)) {
                return@OnLongClickListener if (item is SmartCardData) {
                    @Suppress("UNCHECKED_CAST")
                    onItemLongClick(v, item.data as T, currentPosition)
                } else {
                    onItemLongClick(v, item, currentPosition)
                }
            }
            false
        })
        holder.itemView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return false
            }
        })
    }

    /**
     * Item 是否可点击
     */
    open fun clickable(position: Int) = true

    /**
     * Item 是否可长按
     */
    open fun longClickable(position: Int) = true

    /**
     * 点击Item时候调用
     */
    open fun onItemClick(view: View, item: T, position: Int) {
        // do nothing
    }

    /**
     * Called when a item view has been clicked and held.
     */
    open fun onItemLongClick(view: View, item: T, position: Int) = false

    /**
     * 得到对应位置的ViewHolder
     */
    private fun getPosition(holder: VH) = holder.adapterPosition
}
