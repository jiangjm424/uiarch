package com.grank.uiarch.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

/**
 * 数据绑定的基类，其他继承自己这个类的会自动有对应的layout布局binding的引用
 *
 * @param T : ViewDataBinding
 * @property layoutRes Int
 * @property dataBinding T
 */
abstract class AbsDataBindingFragment<T : ViewDataBinding> : BaseFragment() {
    @get:LayoutRes
    abstract val layoutRes: Int

    protected abstract fun setupView(binding: T)

    protected abstract fun setupData(binding: T, lifecycleOwner: LifecycleOwner)

    protected lateinit var dataBinding:T
    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dataBinding = createBinding(inflater, container)
        setupView(dataBinding)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        setupData(dataBinding, viewLifecycleOwner)
        return dataBinding.root
    }

    private fun createBinding(inflater: LayoutInflater, container: ViewGroup?): T =
        DataBindingUtil.inflate(
            inflater,
            layoutRes,
            container,
            false
        )

    final override fun inherit() = true
}