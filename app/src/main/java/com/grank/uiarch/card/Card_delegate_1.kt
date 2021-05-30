package com.grank.uiarch.card

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grank.smartadapter.delegate.BindingAdapterDelegate
import com.grank.smartadapter.vh.BindingViewHolder
import com.grank.uiarch.R
import com.grank.uiarch.databinding.DashItem1Binding

class Card_delegate_1: BindingAdapterDelegate<String,DashItem1Binding>("1") {
    override val layoutRes: Int
        get() = R.layout.dash_item_1


    override fun configureViewHolder(holder: BindingViewHolder, lifecycleOwner: LifecycleOwner) {
    }

    companion object {
        val TAG = "1"
    }

    override fun setVariable(binding: DashItem1Binding, item: String, position: Int,lifecycleOwner: LifecycleOwner) {
        val map = Gson().fromJson<Map<String, String>>(item)
        binding.desc.text = map["editorIntro$position"]
    }
}