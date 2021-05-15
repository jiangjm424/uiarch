package com.grank.uiarch.activity

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grank.logger.Log
import com.grank.datacenter.net.NetStateManager
import com.grank.uiarch.R
import com.grank.uiarch.databinding.ActivityMainBinding
import com.grank.uiarch.testdi.HiltTest
import com.grank.uiarch.testdi.SelfDi
import com.grank.uicommon.ui.base.AbsDataBindingActivity
import com.grank.uiarch.ui.dashboard.DashboardFragment
import com.grank.uiarch.ui.ext.setupWithFragments
import com.grank.uiarch.ui.home.HomeFragment
import com.grank.uiarch.ui.notifications.NotificationsFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AbsDataBindingActivity<ActivityMainBinding>() {

    @Inject
    lateinit var hiltTest: HiltTest

    @Inject
    lateinit var selfDi: SelfDi

    @Inject
    lateinit var netStateManager: NetStateManager

    private var bottomNavigationView: BottomNavigationView? = null

    private val viewModel:MainViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun setupView(binding: ActivityMainBinding) {
        setupBottomNavigationBar(binding)
        hiltTest.print()
        selfDi.pp()
    }

    override fun setupData(binding: ActivityMainBinding, lifecycleOwner: LifecycleOwner) {
        binding.viewModel = this.viewModel//将数据 绑定到XML布局中，当然这里也可以用livedata.observer(this){}来实现
        netStateManager.netStateLiveData.observe(this) {
            Log.i(it)
        }
    }

    /**
     * 通过jetpack的导航组件来实现可视化导航。
     * 注意，如果是通过这里面实现，则在XML中要去nav_view中指定导航的xml
     */
    private fun setupBottomNavByGraph() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }


    /**
     * 通过自己控件导航
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar(binding: ActivityMainBinding) {
        bottomNavigationView = binding.navView
        bottomNavigationView?.itemIconTintList = null
        bottomNavigationView?.setupWithFragments(
            listOf(
                Pair(R.id.navigation_home, HomeFragment::class.java as Class<Fragment>),
                Pair(R.id.navigation_dashboard, DashboardFragment::class.java as Class<Fragment>),
                Pair(R.id.navigation_notifications, NotificationsFragment::class.java as Class<Fragment>
                )
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
        ) {
        }
        bottomNavigationView?.selectedItemId = R.id.navigation_home
    }

}