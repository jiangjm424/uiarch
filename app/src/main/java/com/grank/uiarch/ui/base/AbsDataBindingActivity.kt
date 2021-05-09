package com.grank.uiarch.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner

/**
 * 数据绑定的基类，其他继承自己这个类的会自动有对应的layout布局binding的引用
 *
 * @param T : ViewDataBinding
 * @property layoutRes Int
 * @property dataBinding T
 */
abstract class AbsDataBindingActivity<T : ViewDataBinding> : BaseActivity() {
    @get:LayoutRes
    abstract val layoutRes: Int

    protected lateinit var dataBinding: T


    /**
     * UI工作的准备工业在这个函数里面实现，比如recyleview的初始化工作
     * @param binding T
     */
    protected abstract fun setupView(binding: T)

    /***
     * 与数据 绑定的工作在这个函数里面实现
     * @param binding T
     * @param lifecycleOwner LifecycleOwner
     */
    protected abstract fun setupData(binding: T, lifecycleOwner: LifecycleOwner)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding = DataBindingUtil.setContentView<T>(this, layoutRes)
        setupView(dataBinding)
        dataBinding.lifecycleOwner = this
        setupData(dataBinding, this)
    }

    final override fun inherit() = true
}