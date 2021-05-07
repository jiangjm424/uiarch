package com.grank.uiarch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grank.logger.Log
import com.grank.uiarch.testdi.HiltTest
import com.grank.uiarch.testdi.SelfDi
import com.grank.uiarch.ui.dashboard.DashboardFragment
import com.grank.uiarch.ui.ext.setupWithFragments
import com.grank.uiarch.ui.home.HomeFragment
import com.grank.uiarch.ui.notifications.NotificationsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var hiltTest: HiltTest

    @Inject lateinit var selfDi: SelfDi

    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setupBottomNavByGraph()
        setupBottomNavigationBar()
        hiltTest.print()
        selfDi.pp()
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                Log.i("adaf","fasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdffasdfasfasdfsfasdfasdf")
            }
        }
    }

    private fun setupBottomNavByGraph() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }


    /**
     * 通过自己控件导航
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView?.itemIconTintList = null
        bottomNavigationView?.setupWithFragments(
            listOf(
                Pair(R.id.navigation_home, HomeFragment::class.java as Class<Fragment>),
                Pair(R.id.navigation_dashboard, DashboardFragment::class.java as Class<Fragment>),
                Pair(R.id.navigation_notifications, NotificationsFragment::class.java as Class<Fragment>)
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
        ) {
        }
        bottomNavigationView?.selectedItemId = R.id.navigation_home
    }
}