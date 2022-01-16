package com.grank.uiarch.ui.ext

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

@Navigator.Name("x_fragment")
class PersistFragmentNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    companion object {
        private const val TAG = "PersistFragmentNavigator"
    }

    override fun popBackStack(): Boolean {
        return true
    }

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (manager.isStateSaved) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already saved its state")
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }
        val ft = manager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        @IdRes val destId = destination.id
        val tag = generateFragmentTag(className, destId)
        val currentFrag = manager.primaryNavigationFragment
        currentFrag?.let {
            ft.setMaxLifecycle(currentFrag, Lifecycle.State.STARTED)
            ft.hide(currentFrag)
        }
        var destFrag = manager.findFragmentByTag(tag)
        if (destFrag == null) {
            destFrag =
                instantiateFragment(context, manager, className, args).apply { arguments = args }
            ft.add(containerId, destFrag, tag)
            ft.setMaxLifecycle(destFrag, Lifecycle.State.RESUMED)
        } else {
            ft.show(destFrag)
            ft.setMaxLifecycle(destFrag, Lifecycle.State.RESUMED)
        }
        Log.i("jiang", "dest:$destFrag $className")

        ft.setPrimaryNavigationFragment(destFrag)

        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key!!, value!!)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        return destination
    }


    override fun createDestination(): Destination {
        return Destination(this)
    }

    private fun generateFragmentTag(className: String, destId: Int): String {
        return "$className-$destId"
    }

    //不支持返回栈
//    private val mBackStack: ArrayDeque<Int>? by lazy {
//        try {
//            val mBackStackField = FragmentNavigator::class.java.getDeclaredField("mBackStack")
//            mBackStackField.isAccessible = true
//            mBackStackField.get(this) as ArrayDeque<Int>
//        } catch (ex: NoSuchFieldException) {
//            null
//        }
//    }
}