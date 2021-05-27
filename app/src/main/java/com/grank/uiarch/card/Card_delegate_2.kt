package com.grank.uiarch.card

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.grank.smartadapter.delegate.BindingAdapterDelegate
import com.grank.smartadapter.vh.BindingViewHolder
import com.grank.uiarch.R
import com.grank.uiarch.databinding.DashItem1Binding
import com.grank.uiarch.databinding.DashItem2Binding
import com.grank.uicommon.util.loadImage
import com.grank.uicommon.util.loadRoundImage

class Card_delegate_2: BindingAdapterDelegate<String,DashItem2Binding>("50001") {
    override val layoutRes: Int
        get() = R.layout.dash_item_2


    override fun configureViewHolder(holder: BindingViewHolder, lifecycleOwner: LifecycleOwner) {
        val binding = holder.getBinding<DashItem2Binding>()
    }
    companion object {
        val TAG = "50001"
    }

    override fun setVariable(binding: DashItem2Binding, item: String, position: Int) {
        val map = Gson().fromJson<Map<String,String>>(item)
        binding.icon.loadImage(map["iconUrl$position"])
    }
}