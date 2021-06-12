package com.grank.uiarch.ui.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.grank.datacenter.db.DemoEntity
import com.grank.datacenter.model.Article
import com.grank.datacenter.net.Resource
import com.grank.logger.Log
import com.grank.uiarch.R
import com.grank.uiarch.databinding.FragmentHomeBinding
import com.grank.uiarch.testdi.HiltTest
import com.grank.uiarch.testdi.SelfDi
import com.grank.uiarch.testdi.log
import com.grank.uicommon.ui.GToast
import com.grank.uicommon.ui.base.AbsDataBindingFragment
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : AbsDataBindingFragment<FragmentHomeBinding>() {

    override val layoutRes: Int = R.layout.fragment_home

    private val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var homeArticleAdapter: HomeArticleAdapter

    @Inject
    lateinit var gToast: GToast

    override fun setupView(binding: FragmentHomeBinding) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = homeArticleAdapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            Log.i("jiang","setOnRefreshListener")
            homeViewModel.getHomeArticles()
        }
        binding.swipeRefreshLayout.setOnLoadMoreListener {
            Log.i("jiang","next setOnLoadMoreListener")
            loadNextPageData()
        }

    }

    private fun loadNextPageData() {
        homeViewModel.loadNextPage().observe(viewLifecycleOwner) {
            if (it.status == Resource.Status.SUCCESS) {
                Log.i("jiang","next sucess")
                homeArticleAdapter.add(it.data!!)
                dataBinding.swipeRefreshLayout.finishLoadMore(true)
            } else if (it.status == Resource.Status.FAIL) {
                Log.i("jiang","next faile")
                dataBinding.swipeRefreshLayout.finishLoadMore(false)
            }
        }
    }

    override fun setupData(binding: FragmentHomeBinding, lifecycleOwner: LifecycleOwner) {
        homeViewModel.articles.observe(lifecycleOwner) {
            homeArticleAdapter.add(it!!, true)
            dataBinding.swipeRefreshLayout.finishRefresh(true)
        }
    }

    override fun onPageFirstComing() {
        homeViewModel.getHomeArticles()
    }

    override fun destroyView(binding: FragmentHomeBinding) {

    }
}