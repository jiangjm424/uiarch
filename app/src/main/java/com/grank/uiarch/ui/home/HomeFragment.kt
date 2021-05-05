package com.grank.uiarch.ui.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.grank.uiarch.R
import com.grank.uiarch.databinding.FragmentHomeBinding
import com.grank.uiarch.ui.base.AbsDataBindingFragment

class HomeFragment : AbsDataBindingFragment<FragmentHomeBinding>() {

    override val layoutRes: Int = R.layout.fragment_home

    private val homeViewModel: HomeViewModel by viewModels()

    override fun setupView(binding: FragmentHomeBinding) {
        val sectionsPagerAdapter = SectionsPagerAdapter(requireContext(), childFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(binding.viewPager)
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun setupData(binding: FragmentHomeBinding, lifecycleOwner: LifecycleOwner) {

    }
}