/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grank.uiarch.ui.ext

import android.util.SparseArray
import android.view.MenuItem
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.google.android.material.bottomnavigation.BottomNavigationView
/**
 * 在BottomNavigationView上管理的fragment使用懒加载的方式，
 * 懒加载实现的方案：
 * 在目前情况下，fragment加载到布局中后，会一直跑到onResume状态，如果加载多个且不去hide部分时，则会出现重叠等
 * 现象，而onResume时表示fragment显示到了屏幕上，这里也是我们想开始加载数据的地方，但是只是首次onResume时才加载
 * 因此懒加载的实现可以让没有显示的fragment只到start状态，当前显示的fragment才到resume状态，按以下两种情况处理：
 * 在初始化过程
 * 1 先将所有的fragment添加到containerId上，同时将fragment的最大生命周期置start
 * 2 找到当前需要显示的fragment,修改其最大生命周期为resume
 * 3 commit提交
 * 在点击过程
 * 1 将当前的生命周期修改为start （点击还未生效时的当前fragment)
 * 2 修改当前fragment对象，并改成resume
 * 3 commit
 * @receiver BottomNavigationView
 * @param fragmentList List<Pair<Int, Class<T>>>
 * @param fragmentManager FragmentManager
 * @param containerId Int
 * @param navItemSelectListener Function1<[@kotlin.ParameterName] MenuItem, Unit> */
fun <T : Fragment> BottomNavigationView.setupWithFragments(
    fragmentList: List<Pair<Int, Class<T>>>,
    fragmentManager: FragmentManager,
    containerId: Int,
    navItemSelectListener: ((item: MenuItem) -> Unit)
) {
    // Map of tags
    val navMenuItemIdToTagMap = SparseArray<String>()
    // First create a NavHostFragment for each NavGraph ID
    addFragments(fragmentList, fragmentManager, containerId, navMenuItemIdToTagMap)

    // Now connect selecting an item with swapping Fragments
    var selectedItemTag = navMenuItemIdToTagMap[this.selectedItemId]

    // When a navigation item is selected
    setOnNavigationItemSelectedListener { item ->
        // Don't do anything if the state is state has already been saved.
        if (fragmentManager.isStateSaved) {
            return@setOnNavigationItemSelectedListener false
        }
        val newlySelectedItemTag = navMenuItemIdToTagMap[item.itemId]
        if (selectedItemTag == newlySelectedItemTag) {
            return@setOnNavigationItemSelectedListener false
        }
        // 一个都不能保留 后退栈 全部销毁
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        // 只Attach 新选中的 fragment 其它 fragment全部detach
        showSelectItem(fragmentManager, navMenuItemIdToTagMap, newlySelectedItemTag)
        selectedItemTag = newlySelectedItemTag
        navItemSelectListener.invoke(item)
        true
    }

    // Optional: on item reselected, pop back stack to the destination of the graph
    setupItemReselected(navMenuItemIdToTagMap, fragmentManager)
}

private fun showSelectItem(
    fragmentManager: FragmentManager,
    navMenuItemIdToTagMap: SparseArray<String>,
    newlySelectedItemTag: String,
) {
    val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)
    selectedFragment?.let { select ->
        val transaction = fragmentManager.beginTransaction()
        transaction.setPrimaryNavigationFragment(select)
        navMenuItemIdToTagMap.forEach { _, fragmentTag ->
            if (fragmentTag != newlySelectedItemTag) {
                val fragment = fragmentManager.findFragmentByTag(fragmentTag)!!
                transaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
                transaction.hide(fragment)
            } else {
                val fragment = fragmentManager.findFragmentByTag(fragmentTag)!!
                transaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
                transaction.show(fragment)
            }
        }
        transaction.commitNow()
    }
}

private fun <T : Fragment> BottomNavigationView.addFragments(
    fragmentList: List<Pair<Int, Class<T>>>,
    fragmentManager: FragmentManager,
    containerId: Int,
    navMenuItemIdToTagMap: SparseArray<String>
) {
    val transaction = fragmentManager.beginTransaction()
    fragmentList.forEachIndexed { index, fragmentParam ->
        val fragmentTag = getFragmentTag(index)
        val fragmentNavItemId = fragmentParam.first
        val fragmentClass = fragmentParam.second
        // Find or create the Navigation host fragment
        val fragment = obtainFragment(fragmentManager, fragmentClass, fragmentTag, containerId)
        transaction.add(containerId, fragment, fragmentTag)
        // Save to the map
        navMenuItemIdToTagMap[fragmentNavItemId] = fragmentTag
        if (this.selectedItemId == fragmentNavItemId) {
            transaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
        } else {
            transaction.hide(fragment)
            transaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
        }
    }
    transaction.commitNow()
}

private fun BottomNavigationView.setupItemReselected(
    graphIdToTagMap: SparseArray<String>,
    fragmentManager: FragmentManager
) {
    setOnNavigationItemReselectedListener { item ->
//        val newlySelectedItemTag = graphIdToTagMap[item.itemId]
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

private fun <T : Fragment> obtainFragment(
    fragmentManager: FragmentManager,
    fragmentClass: Class<T>,
    fragmentTag: String,
    containerId: Int
): Fragment {
    val existingFragment = fragmentManager.findFragmentByTag(fragmentTag)
    existingFragment?.let { return it }
    val fragment =
        fragmentManager.fragmentFactory.instantiate(fragmentClass.classLoader!!, fragmentClass.name)
//    fragment.arguments = fragmentArgs
//    fragmentManager.beginTransaction()
//        .add(containerId, fragment, fragmentTag)
//        .commitNow()
    return fragment
}

private fun hideFragment(
    fragmentManager: FragmentManager,
    fragment: Fragment
) {
    fragmentManager.beginTransaction()
        .hide(fragment)
        .commitNow()
}

private fun showFragment(
    fragmentManager: FragmentManager,
    fragment: Fragment,
    isPrimaryNavFragment: Boolean
) {
    fragmentManager.beginTransaction()
        .show(fragment)
        .apply {
            if (isPrimaryNavFragment) {
                setPrimaryNavigationFragment(fragment)
            }
        }
        .commitNow()
}

fun getFragmentTag(index: Int) = "bottomNavigation#$index"
