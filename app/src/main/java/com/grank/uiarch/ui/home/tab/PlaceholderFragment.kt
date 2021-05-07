package com.grank.uiarch.ui.home.tab

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.grank.uiarch.R
import com.grank.uiarch.databinding.FragmentTabCommonBinding
import com.grank.uiarch.testdi.HiltTest
import com.grank.uiarch.testdi.SelfDi
import com.grank.uiarch.ui.base.AbsDataBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class PlaceholderFragment : AbsDataBindingFragment<FragmentTabCommonBinding>() {

    @Inject lateinit var hiltTest: HiltTest

    @Inject lateinit var selfDi: SelfDi

    private val pageViewModel: PageViewModel by viewModels<PageViewModel>()


    override val layoutRes: Int
        get() = R.layout.fragment_tab_common

    override fun setupView(binding: FragmentTabCommonBinding) {
        pageViewModel.setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
    }

    override fun setupData(binding: FragmentTabCommonBinding, lifecycleOwner: LifecycleOwner) {
        pageViewModel.text.observe(viewLifecycleOwner) {
            binding.sectionLabel.text = it
        }
        hiltTest.print()
        selfDi.pp()
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

}