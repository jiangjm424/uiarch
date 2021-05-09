package com.grank.uiarch.ui.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.grank.logger.Log
import com.grank.netcore.ServerApi
import com.grank.netcore.core.Resource
import com.grank.uiarch.R
import com.grank.uiarch.databinding.FragmentHomeBinding
import com.grank.uiarch.testdi.HiltTest
import com.grank.uiarch.testdi.SelfDi
import com.grank.uiarch.ui.base.AbsDataBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : AbsDataBindingFragment<FragmentHomeBinding>() {

    @Inject lateinit var hiltTest: HiltTest

    @Inject lateinit var selfDi: SelfDi

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
            hiltTest.print()
            selfDi.pp()
            homeViewModel.getState()
        }
    }

    override fun setupData(binding: FragmentHomeBinding, lifecycleOwner: LifecycleOwner) {

        homeViewModel.text.observe(this) {
            if (it.status == Resource.Status.SUCCESS) {
                Log.v("sucess:${it.data?.cstateno}")
            }
            Log.v("res:$it")
        }
    }
}