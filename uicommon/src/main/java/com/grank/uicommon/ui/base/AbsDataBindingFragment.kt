package com.grank.uicommon.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner

/**
 * 数据绑定的基类，其他继承自己这个类的会自动有对应的layout布局binding的引用
 * 注意三个方法的使用，
 * 1 seupView是用于对view做初始化操作
 * 2 destoryview是销毁操作，比如在setupview中添加了一个listener, 并且需要退出页面再删除这listener时，则在
 * destoryView中删除，如果不需要这种操作，那么可以不实现这个方法，所以这里面这个方法不设置为虚函数
 * 3 setupdata是用于绑定需要监听的livedata时调用的
 * @param T : ViewDataBinding
 * @property layoutRes Int
 * @property dataBinding T
 */
abstract class AbsDataBindingFragment<T : ViewDataBinding> : BaseFragment() {

    @get:LayoutRes
    abstract val layoutRes: Int

    protected abstract fun setupView(binding: T)

    protected abstract fun setupData(binding: T, lifecycleOwner: LifecycleOwner)


    protected open fun destroyView(binding: T) {}

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

    final override fun onDestroyView() {
        super.onDestroyView()
        destroyView(dataBinding)
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