package com.grank.uiarch.ui.home

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.grank.uiarch.R
import com.grank.uiarch.ui.home.tab.PlaceholderFragment
import com.grank.uiarch.ui.home.tab.TabWebFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 *
 * 首页page建议在进入时根据后台要求动态创建
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_2,
        R.string.tab_text_2,
        R.string.tab_text_3
    )
    private val fragments = arrayOf(
        PlaceholderFragment.newInstance(1),
//        PlaceholderFragment.newInstance(2),
//        PlaceholderFragment.newInstance(3),
//        PlaceholderFragment.newInstance(4),
        TabWebFragment()
    )
    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return fragments?.size ?: 0
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //这里面不要销毁fragment,以免fragment重建，导致状态丢失
    }
}