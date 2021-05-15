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
import com.google.android.material.bottomnavigation.BottomNavigationView

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
    val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)!!
    fragmentManager.beginTransaction()
        // 问题暂时未明确
//        .setCustomAnimations(
//            R.anim.fragment_enter_anim,
//            R.anim.fragment_exit_anim,
//            R.anim.fragment_pop_enter_anim,
//            R.anim.fragment_pop_exit_anim
//        )
        .show(selectedFragment)
        .setPrimaryNavigationFragment(selectedFragment)
        .apply {
            // Detach all other Fragments
            navMenuItemIdToTagMap.forEach { _, fragmentTag ->
                if (fragmentTag != newlySelectedItemTag) {
                    hide(fragmentManager.findFragmentByTag(fragmentTag)!!)
                }
            }
        }
        .commit()
}

private fun <T : Fragment> BottomNavigationView.addFragments(
    fragmentList: List<Pair<Int, Class<T>>>,
    fragmentManager: FragmentManager,
    containerId: Int,
    navMenuItemIdToTagMap: SparseArray<String>
) {
    fragmentList.forEachIndexed { index, fragmentParam ->
        val fragmentTag = getFragmentTag(index)
        val fragmentNavItemId = fragmentParam.first
        val fragmentClass = fragmentParam.second
        // Find or create the Navigation host fragment
        val fragment = obtainFragment(fragmentManager, fragmentClass, fragmentTag, containerId)
        // Save to the map
        navMenuItemIdToTagMap[fragmentNavItemId] = fragmentTag
        if (this.selectedItemId == fragmentNavItemId) {
            showFragment(fragmentManager, fragment, index == 0)
        } else {
            hideFragment(fragmentManager, fragment)
        }
    }
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
    val fragment = fragmentManager.fragmentFactory.instantiate(fragmentClass.classLoader!!, fragmentClass.name)
//    fragment.arguments = fragmentArgs
    fragmentManager.beginTransaction()
        .add(containerId, fragment, fragmentTag)
        .commitNow()
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
