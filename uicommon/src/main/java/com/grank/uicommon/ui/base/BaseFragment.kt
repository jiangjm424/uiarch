package com.grank.uicommon.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.grank.logger.Log

/**
 * 提供基础功能的fragment,提供生命周期打印功能。
 * 实际项目中不要通过这个类继续
 * 所有实际的使用的fragment都应该继承自AbsDataBindingFragment
 */
abstract class BaseFragment : Fragment() {

    companion object {
        private const val TAG = "BaseFragment"
        private const val DEBUG = true
    }

    private fun log(msg: String) {
        if (DEBUG) {
            Log.v(TAG, msg)
        }
    }

    /**
     * 防止业务实现方直接继承该类
     * @return Boolean
     */
    protected open fun inherit() = false
    override fun onAttach(context: Context) {
        super.onAttach(context)
        log(" onAttach $this")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log(" onCreate $this")
        if (!inherit()) {
            throw IllegalStateException("you can not inherit BaseFragment directly, but use AbsDataBindingFragment instead!!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        log(" onCreateView $this")
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log(" onViewCreated $this")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        log(" onActivityCreated $this")
    }

    override fun onStart() {
        super.onStart()
        activity?.apply {
//            StatusBarHelper.translucent(this)
//            StatusBarHelper.setStatusBarDarkMode(this)
        }
        log(" onStart $this")
    }

    override fun onResume() {
        super.onResume()
        log(" onResume $this")
    }

    override fun onPause() {
        log(" onPause $this")
        super.onPause()
    }

    override fun onStop() {
        log(" onStop $this")
        super.onStop()
    }

    override fun onDetach() {
        log(" onDetach $this")
        super.onDetach()
    }

    override fun onDestroyView() {
        log(" onDestroyView $this")
        super.onDestroyView()
    }

    override fun onDestroy() {
        log(" onDestroy $this")
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        val hideStr = if (hidden) "hide" else "show"
        log(" onHiddenChanged $hideStr $this")
        super.onHiddenChanged(hidden)
    }

    protected fun addFragment(fragment: BaseFragment, @IdRes containerId: Int) {
        if (isStateSaved) {
            log("state already saved, can not add fragment again, fragment: $fragment")
            return
        }
        parentFragmentManager.beginTransaction()
            .add(containerId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }

    protected fun removeFragment(fragment: BaseFragment) {
        if (isStateSaved) {
            log("state already saved, can not remove fragment again, fragment: $fragment")
            return
        }
        parentFragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
    }

    protected fun replaceFragment(fragment: BaseFragment, @IdRes containerId: Int) {
        if (isStateSaved) {
            log("state already saved, can not replace fragment again, fragment: $fragment")
            return
        }
        parentFragmentManager.beginTransaction()
            .replace(containerId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }

}