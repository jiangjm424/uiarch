
package com.grank.smartadapter.vh

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/*
 * -----------------------------------------------------------------
 * description：ViewHolder databinding实现基类
 * -----------------------------------------------------------------
 */
class BindingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var binding: ViewDataBinding? = null
    var cardId = ""
    var cardTitle = ""

    fun <T : ViewDataBinding> getBinding(): T {
        @Suppress("UNCHECKED_CAST")
        return binding as T
    }

    fun setBinding(binding: ViewDataBinding) {
        this.binding = binding
    }
}
