package com.grank.uicommon.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope

/**
 * 数据绑定的基类，其他继承自己这个类的会自动有对应的layout布局binding的引用
 * 注意以下几个方法的使用，
 * 1 seupView是用于对view做初始化操作
 * 2 destoryview是销毁操作，比如在setupview中添加了一个listener, 并且需要退出页面再删除这listener时，则在
 *   destoryView中删除，
 * 3 setupdata是用于绑定需要监听的livedata时调用的
 * 4 onPageFirstComing 用于懒加载数据，当页面显示onResume只会调用一次。
 *   可以在这里面调用请求数据的接口。当然如果想提前加载数据，可以在setupView中请求数据
 * @param T : ViewDataBinding
 * @property layoutRes Int
 * @property dataBinding T
 */
abstract class AbsDataBindingFragment<T : ViewDataBinding> : BaseFragment() {

    @get:LayoutRes
    abstract val layoutRes: Int

    protected abstract fun setupView(binding: T)

    protected abstract fun setupData(binding: T, lifecycleOwner: LifecycleOwner)

    /***
     * 在使用tabLayout+viewpager时， 只有第一个fragment才会执行到onResume,其他fragment只能到onStart
     * 当fragment在resume后才会执行一次，一般用来触发懒加载数据
     */
    protected abstract fun onPageFirstComing()

    protected abstract fun destroyView(binding: T)

    protected lateinit var dataBinding: T

    /**
     * fragment自身也实现了lifecycleOwer的接口，我们可以直接使用lifecycleScope
     * 但是这个协程域和fragment实例的生命周期一样的，也即当这个fragment被deattached后仍然存在，所以为了让
     * fragment与其对应的view有同样的生命周期，google又在fragment中定义了一个viewLifecycleOwner的变量
     * 用于跟踪其对应的view, 所以为了方便，我这里面定义一个与viewLifecycleOwner对应的协程
     */
    protected val viewLifecycleScope
        get() = viewLifecycleOwner.lifecycleScope

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
        viewLifecycleScope.launchWhenResumed {
            onPageFirstComing()
        }
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