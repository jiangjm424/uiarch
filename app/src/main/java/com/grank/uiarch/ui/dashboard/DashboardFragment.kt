package com.grank.uiarch.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grank.uiarch.R
import com.grank.uiarch.databinding.FragmentDashboardBinding
import com.grank.uiarch.ui.base.AbsDataBindingFragment

class DashboardFragment : AbsDataBindingFragment<FragmentDashboardBinding>() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.fragment_dashboard

    override fun setupView(binding: FragmentDashboardBinding) {
    }

    override fun setupData(binding: FragmentDashboardBinding, lifecycleOwner: LifecycleOwner) {
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textDashboard.text = it
        })
    }
}