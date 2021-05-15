package com.grank.smartadapter.delegate

import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.grank.smartadapter.SmartCardData
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

/*
 * -----------------------------------------------------------------
 * ViewCard主要管理类，受AbsBaseAdapter委托，将不同ViewCard数据结构
 * 保存记录，维护ViewCard list数据结构，主要是把不同数据结构
 * 和对应的显示模板做关联记录
 * -----------------------------------------------------------------
 */
class AdapterManager {

    private val dataTypeAndTags = SparseArray<String>()
    private val delegates = SparseArrayCompat<CardAdapterDelegate<Any, RecyclerView.ViewHolder>>()
    var backDelegate: CardAdapterDelegate<Any, RecyclerView.ViewHolder>? =
        Card_Delegate_default() as CardAdapterDelegate<Any, RecyclerView.ViewHolder>

    /**
     * 将不同delegate按照顺序存入delegates
     * 将delegate中的数据类型，按照delegate存入序号 对应存入dataTypeWithTags中
     * @param delegate
     */
    fun addDelegate(delegate: CardAdapterDelegate<*, *>, tag: String): AdapterManager {
        val superclass = delegate.javaClass.genericSuperclass
        try {
            val clazz = (superclass as ParameterizedType).actualTypeArguments[0] as Type    // 从抽象类取出delegate真实的类
            val typeWithTag = type2WithTag(clazz, tag)       // delegate对象是否有tag 没有就用类名做Tag
            val viewType = delegates.size()
            // 保存委托到集合;
            @Suppress("UNCHECKED_CAST")
            delegates.put(viewType, delegate as CardAdapterDelegate<Any, RecyclerView.ViewHolder>?)
            // 将委托的索引保存到集合
            dataTypeAndTags.put(viewType, typeWithTag)

        } catch (e: Exception) {
            Log.e("AdapterManager", "add delegate", e)
            // 没有泛型或者泛型不正确.
            throw IllegalArgumentException("Please set the correct generic parameters on ${delegate.javaClass.name}.")
        }

        return this
    }

    fun onCreateViewHolder(lifecycleOwner: LifecycleOwner, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegate = getDelegate(viewType)
            ?: throw NullPointerException("No CardAdapterDelegate added for ViewType $viewType")

        // 在Java中可以为空
        return delegate.onCreateViewHolder(parent, lifecycleOwner)
            ?: throw NullPointerException(
                "ViewHolder returned from CardAdapterDelegate ${delegate.javaClass}"
                        + " for ViewType = $viewType is null!"
            )
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, item: Any) {
        val viewType = holder.itemViewType
        val delegate = getDelegate(viewType)
            ?: throw NullPointerException(
                "No delegate found for item at position = "
                        + position
                        + " for viewType = "
                        + viewType
            )
        delegate.onBindViewHolder(holder, position, item)
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>?, item: Any) {
        val viewType = holder.itemViewType
        val delegate = getDelegate(viewType)
            ?: throw NullPointerException("No delegate found for item at position = $position for viewType = $viewType")
        delegate.onBindViewHolder(holder, position, payloads, item)
    }

    /**
     * 返回项的视图类型
     */
    fun getItemViewType(item: Any, position: Int): Int {
        var startTime = System.currentTimeMillis()

        val clazz = targetClass(item)
        val tag = targetTag(item)

        val typeWithTag = type2WithTag(clazz, tag)

        val indexList = indexesOfValue(dataTypeAndTags, typeWithTag)
        indexList.forEach {
            val delegate = delegates.valueAt(it)
            if (delegate?.tag == tag && delegate.isForViewType(item, position)) {
                return it
            }
        }

        // 如果没有为数据类型添加AdapterDelegate，则返回最大的视图类型 + 1.
        if (backDelegate != null) {
            Log.w("AdapterManager", "can not find delegate for $typeWithTag")
            return delegates.size()
        }

        throw NullPointerException("No CardAdapterDelegate added that matches position = $position item = $item tag = $tag in data source.")
    }

    fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        val itemViewType = holder?.itemViewType ?: return
        val delegate = getDelegate(itemViewType)
        delegate?.onViewRecycled(holder)
    }

    fun onFailedToRecycleView(holder: RecyclerView.ViewHolder?): Boolean {
        val itemViewType = holder?.itemViewType ?: return false
        val delegate = getDelegate(itemViewType)
        return delegate?.onFailedToRecycleView(holder) ?: false
    }

    fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        val itemViewType = holder?.itemViewType ?: return
        val delegate = getDelegate(itemViewType)
        delegate?.onViewAttachedToWindow(holder)
    }

    fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder?) {
        val itemViewType = holder?.itemViewType ?: return
        val delegate = getDelegate(itemViewType)
        delegate?.onViewDetachedFromWindow(holder)
    }

    fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        for (i in 0 until delegates.size()) {
            val delegate = delegates.get(delegates.keyAt(i))
            delegate!!.onAttachedToRecyclerView(recyclerView)
        }
    }

    fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        for (i in 0 until delegates.size()) {
            val delegate = delegates.get(delegates.keyAt(i))
            delegate!!.onDetachedFromRecyclerView(recyclerView)
        }
    }

    fun setVisibility(boolean: Boolean?) {

        for (i in 0 until delegates.size()) {
            val delegate = delegates.get(delegates.keyAt(i))
            delegate!!.setVisibility(boolean)
        }
    }

    fun onPause() {
        for (i in 0 until delegates.size()) {
            val delegate = delegates.get(delegates.keyAt(i))
            delegate!!.onPause()
        }
    }

    fun onResume() {
        for (i in 0 until delegates.size()) {
            val delegate = delegates.get(delegates.keyAt(i))
            delegate!!.onResume()
        }
    }

    fun onDestroy() {
        for (i in 0 until delegates.size()) {
            val delegate = delegates.get(delegates.keyAt(i))
            delegate!!.onDestroy()
        }
    }

    fun setViewSource(source: String) {
        for (i in 0 until delegates.size()) {
            val delegate = delegates.get(delegates.keyAt(i))
            delegate!!.viewSource = source
        }
    }

    fun getDelegate(viewType: Int): CardAdapterDelegate<Any, RecyclerView.ViewHolder>? =
        delegates.get(viewType, backDelegate)

    private val typeWithTag = { clazz: Class<*>, tag: String ->
        if (tag.isEmpty()) clazz.name else clazz.name + ":" + tag
    }

    private fun getTypeSig(type: Type): String {
        return when (type) {
            is Class<*> -> type.name
            is ParameterizedType -> {
                (type.rawType as Class<*>).name
            }
            else -> {
                type.typeName
            }
        }
    }

    private val type2WithTag = { clazz: Type, tag: String ->
        if (tag.isEmpty()) getTypeSig(clazz) else getTypeSig(clazz) + ":" + tag
    }

    private val targetClass = { data: Any ->
        if (data is SmartCardData) data.data.javaClass else data.javaClass
    }

    private val targetTag = { data: Any ->
        if (data is SmartCardData) data.tag else CardAdapterDelegate.DEFAULT_TAG
    }

    /**
     * 返回指定值的所有索引
     */
    private fun indexesOfValue(array: SparseArray<String>, value: String): ArrayList<Int> {
        val indexes = ArrayList<Int>()

        for (i in 0 until array.size()) {
            if (value == array.valueAt(i)) {
                indexes.add(i)
            }
        }
        return indexes
    }
}
