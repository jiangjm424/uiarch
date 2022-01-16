package com.grank.uiarch.ui.ext

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.grank.uiarch.R

class PersistNavHostFragment : NavHostFragment() {
    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)
        navController.navigatorProvider.addNavigator(
            PersistFragmentNavigator(
                requireContext(), childFragmentManager,
                getContainerId()
            )
        )
    }

    private fun getContainerId(): Int {
        if (id != 0 && id != View.NO_ID) {
            return id
        }
        return R.id.persist_nav_host_fragment_container
    }
}