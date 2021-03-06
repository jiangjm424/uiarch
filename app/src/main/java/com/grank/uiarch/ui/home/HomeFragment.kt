package com.grank.uiarch.ui.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.grank.datacenter.db.DemoEntity
import com.grank.datacenter.net.Resource
import com.grank.logger.Log
import com.grank.uiarch.R
import com.grank.uiarch.databinding.FragmentHomeBinding
import com.grank.uiarch.testdi.HiltTest
import com.grank.uiarch.testdi.SelfDi
import com.grank.uiarch.testdi.log
import com.grank.uicommon.ui.GToast
import com.grank.uicommon.ui.base.AbsDataBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : AbsDataBindingFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var hiltTest: HiltTest

    @Inject
    lateinit var selfDi: SelfDi

    override val layoutRes: Int = R.layout.fragment_home

    private val homeViewModel: HomeViewModel by viewModels()

    var ii: Long = 0
    override fun setupView(binding: FragmentHomeBinding) {
        val sectionsPagerAdapter = SectionsPagerAdapter(requireContext(), childFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(binding.viewPager)
        tabs.tabMode = TabLayout.MODE_SCROLLABLE
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            hiltTest.print()
            hiltTest.log("action" +
                    "cccc")
            selfDi.pp()
//            homeViewModel.getState()
//            homeViewModel.gettoppage()
            gToast.show("kwkwkwkwkwkw")
            homeViewModel.getvv()
//            homeViewModel.add(DemoEntity(ii++, System.currentTimeMillis().toString() + " hh"))
        }
        binding.fab2.setOnClickListener {
           val j= viewLifecycleScope.launch {
                val bb = homeViewModel.geta().collect {
                    Log.i("jiang","flow $it")
                    it
                }
               val cc = homeViewModel.geta().first()

            }
        }
    }

    @Inject
    lateinit var gToast:GToast
    override fun setupData(binding: FragmentHomeBinding, lifecycleOwner: LifecycleOwner) {

        homeViewModel.newAppVersion.observe(viewLifecycleOwner) {
            Log.i(it.toString())
        }
        homeViewModel.text.observe(this) {
            if (it.status == Resource.Status.SUCCESS) {
                Log.v("sucess:${it.data?.cstateno}")
            }
            Log.v("res:$it")
        }
        homeViewModel.toppage.observe(viewLifecycleOwner) {
            Log.i("jiang","$it")
        }
        lifecycleScope.launchWhenCreated {

            homeViewModel.getallDemo().collect {
                it.forEach {
                    Log.i("jiang", "entity: ${it.name}")
                }
            }
        }
    }

    override fun onPageFirstComing() {
        Log.i("jiang","home page first coming")
    }

    override fun destroyView(binding: FragmentHomeBinding) {

    }
}