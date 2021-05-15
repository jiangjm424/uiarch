package com.grank.smartadapter.adapter

import androidx.lifecycle.LifecycleOwner
import java.util.*

/*
 * 管理每个AdapterDelegate
 */
open class SmartAdapter(lifecycleOwner: LifecycleOwner) : AbsSmartAdapter(lifecycleOwner) {

    private var dataItems: MutableList<Any> = ArrayList()
    private var headerItems: MutableList<Any> = ArrayList()
    private var footerItems: MutableList<Any> = ArrayList()

    val dataCount: Int
        get() = dataItems.size

    val headerCount: Int
        get() = headerItems.size

    val footerCount: Int
        get() = footerItems.size

    fun setHeaderItem(headerItem: Any) {
        headerItems.clear()
        headerItems.add(headerItem)
        notifyDataSetChanged()
    }

    fun setHeaderItems(headerItems: MutableList<*>) {
        this.headerItems.clear()
        this.headerItems.addAll(headerItems.filterNotNull())
        notifyDataSetChanged()
    }

    fun addHeaderItem(headerItem: Any) {
        addHeaderItem(headerCount, headerItem)
    }

    fun addHeaderItem(position: Int = headerCount, headerItem: Any) {
        headerItems.add(position, headerItem)
        notifyItemRangeInserted(position, 1)
    }

    fun addHeaderItems(headerItems: MutableList<*>) {
        addHeaderItems(headerCount, headerItems)
    }

    fun addHeaderItems(position: Int = headerCount, headerItems: MutableList<*>) {
        this.headerItems.addAll(position, headerItems.filterNotNull())
        notifyItemRangeInserted(position, headerItems.size)
    }

    fun setFooterItem(footerItem: Any) {
        footerItems.clear()
        footerItems.add(footerItem)
        notifyDataSetChanged()
    }

    fun setFooterItems(footerItems: MutableList<*>) {
        this.footerItems.clear()
        this.footerItems.addAll(footerItems.filterNotNull())
        notifyDataSetChanged()
    }

    fun addFooterItem(footerItem: Any) {
        addFooterItem(footerCount, footerItem)
    }

    fun addFooterItem(position: Int, footerItem: Any) {
        footerItems.add(position, footerItem)
        notifyItemRangeInserted(headerCount + dataCount + position, 1)
    }

    fun addFooterItems(footerItems: MutableList<*>) {
        addFooterItems(footerCount, footerItems)
    }

    fun addFooterItems(position: Int, footerItems: MutableList<*>) {
        this.footerItems.addAll(position, footerItems.filterNotNull())
        notifyItemRangeInserted(headerCount + dataCount + position, footerItems.size)
    }

    fun setDataItems(dataItems: MutableList<*>) {
        this.dataItems.clear()
        this.dataItems.addAll(dataItems.filterNotNull())
        notifyDataSetChanged()
    }

    fun updateDataItem(position: Int, item: Any) {
        this.dataItems[position] = item
        notifyItemChanged(position)
    }

    fun addDataItem(item: Any) {
        addDataItem(dataCount, item)
    }

    fun addDataItem(position: Int = dataCount, item: Any) {
        dataItems.add(position, item)
        notifyItemRangeInserted(headerCount + position, 1)
    }

    fun addDataItems(dataItems: MutableList<*>) {
        addDataItems(dataCount, dataItems)
    }

    fun addDataItems(position: Int = dataCount, dataItems: MutableList<*>) {
        this.dataItems.addAll(position, dataItems.filterNotNull())
        notifyItemRangeInserted(headerCount + position, dataItems.size)
    }

    fun moveDataItem(fromPosition: Int, toPosition: Int) {
        var moveToPosition = toPosition
        moveToPosition = if (fromPosition < moveToPosition) moveToPosition - 1 else moveToPosition
        dataItems.add(moveToPosition, dataItems.removeAt(fromPosition))
        notifyItemMoved(fromPosition, moveToPosition)
    }

    fun removeDataItem(dataItem: Any) {
        val index = dataItems.indexOf(dataItem)
        if (index != -1 && index <= dataCount) {
            removeDataItemAt(index)
        }
    }

    @JvmOverloads
    fun removeDataItemAt(position: Int, itemCount: Int = 1) {
        for (i in 0 until itemCount) {
            dataItems.removeAt(position)
        }
        notifyItemRangeRemoved(headerCount + position, itemCount)
    }

    fun getDataItems() = dataItems

    fun getHeaderItems() = headerItems

    fun getFooterItems() = footerItems

    override fun getItem(position: Int): Any {
        if (position < headerCount) {
            return headerItems[position]
        }

        var offsetPosition = position - headerCount
        if (offsetPosition < dataCount) {
            return dataItems[offsetPosition]
        }

        offsetPosition -= dataCount
        return footerItems[offsetPosition]
    }

    override fun getItemCount() = headerCount + dataCount + footerCount

    fun clearData() = dataItems.clear()

    fun clearHeader() = headerItems.clear()

    fun clearFooter() = footerItems.clear()

    fun clearAllData() {
        clearData()
        clearHeader()
        clearFooter()
    }

}
