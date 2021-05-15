package com.grank.smartadapter.delegate


import androidx.databinding.ViewDataBinding
import com.grank.smartadapter.R


class Card_Delegate_default : BindingAdapterDelegate<String>(
    TAG
) {

    override val layoutRes: Int
        get() = R.layout.card_default

    override fun setVariable(binding: ViewDataBinding, item: String, position: Int) {

    }
    companion object {
        val TAG = "99999"
    }

}



