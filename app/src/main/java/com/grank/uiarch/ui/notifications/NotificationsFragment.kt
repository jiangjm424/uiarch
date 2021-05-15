package com.grank.uiarch.ui.notifications

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.grank.uiarch.R
import com.grank.uiarch.databinding.FragmentNotificationsBinding
import com.grank.uicommon.ui.base.AbsDataBindingFragment

class NotificationsFragment : AbsDataBindingFragment<FragmentNotificationsBinding>() {

    private val notificationsViewModel: NotificationsViewModel by viewModels()

    override val layoutRes: Int = R.layout.fragment_notifications

    override fun setupView(binding: FragmentNotificationsBinding) {
    }

    override fun setupData(binding: FragmentNotificationsBinding, lifecycleOwner: LifecycleOwner) {
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textNotifications.text = it
        })
    }
}