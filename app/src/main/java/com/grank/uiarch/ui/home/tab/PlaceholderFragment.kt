package com.grank.uiarch.ui.home.tab

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.grank.logger.Log
import com.grank.smartadapter.adapter.SmartAdapter
import com.grank.uiarch.R
import com.grank.uiarch.card.Card_delegate_1
import com.grank.uiarch.card.Card_delegate_2
import com.grank.uiarch.databinding.FragmentTabCommonBinding
import com.grank.uiarch.testdi.HiltTest
import com.grank.uiarch.testdi.SelfDi
import com.grank.uiarch.ui.home.HomeViewModel
import com.grank.uicommon.ui.base.AbsDataBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class PlaceholderFragment : AbsDataBindingFragment<FragmentTabCommonBinding>() {

    private lateinit var adapter: SmartAdapter
    @Inject lateinit var hiltTest: HiltTest

    @Inject lateinit var selfDi: SelfDi

    private val pageViewModel: PageViewModel by viewModels<PageViewModel>()

    private val homeviewmodel:HomeViewModel by viewModels( {  requireParentFragment()})

    override val layoutRes: Int
        get() = R.layout.fragment_tab_common

    override fun setupView(binding: FragmentTabCommonBinding) {
        pageViewModel.setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SmartAdapter(viewLifecycleOwner).apply {
            addDelegate(Card_delegate_1(),"1")
            addDelegate(Card_delegate_2(),"50001")
        }
        binding.sectionLabel.setOnClickListener {
            homeviewmodel.gettoppage()
        }
        binding.rv.adapter = adapter
    }

    override fun onPageFirstComing() {
        Log.i("Base","loadingDataWhenResume")
        Log.i("jiang","loadingDataWhenResume")
    }
    override fun setupData(binding: FragmentTabCommonBinding, lifecycleOwner: LifecycleOwner) {
        pageViewModel.text.observe(viewLifecycleOwner) {
            binding.sectionLabel.text = it
        }
        hiltTest.print()
        selfDi.pp()
        homeviewmodel.toppage.observe(viewLifecycleOwner) {
            if (it.status == com.grank.datacenter.net.Resource.Status.SUCCESS) {
                 adapter.setDataItems(it.data!!)
            }
        }
    }
    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun destroyView(binding: FragmentTabCommonBinding) {

    }

}