package com.grank.smartadapter.delegate


import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.grank.smartadapter.R
import com.grank.smartadapter.databinding.CardDefaultBinding
import com.grank.smartadapter.vh.BindingViewHolder


class Card_Delegate_default : BindingAdapterDelegate<String,CardDefaultBinding>(
    TAG
) {

    override val layoutRes: Int
        get() = R.layout.card_default

    companion object {
        val TAG = "99999"
    }

    override fun configureViewHolder(holder: BindingViewHolder, lifecycleOwner: LifecycleOwner) {

    }

    override fun setVariable(binding: CardDefaultBinding, item: String, position: Int) {
    }

}



