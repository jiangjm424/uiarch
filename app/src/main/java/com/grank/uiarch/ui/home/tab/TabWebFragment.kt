package com.grank.uiarch.ui.home.tab

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.grank.uiarch.R
import com.grank.uiarch.databinding.FragmentTabWebBinding
import com.grank.uiarch.ui.base.AbsDataBindingFragment
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient

class TabWebFragment : AbsDataBindingFragment<FragmentTabWebBinding>() {
    private val viewModel: TabWebViewModel by viewModels()
    private var mAgentWeb: AgentWeb? = null
    override val layoutRes: Int
        get() = R.layout.fragment_tab_web

    override fun setupView(binding: FragmentTabWebBinding) {
//
    }

    override fun setupData(binding: FragmentTabWebBinding, lifecycleOwner: LifecycleOwner) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAgentWeb =
            AgentWeb.with(this)
                .setAgentWebParent(
                    dataBinding.webContainer,
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                .useDefaultIndicator()
                .setWebView(null)
                .setWebLayout(null)
                .setAgentWebWebSettings(null)
                .setWebViewClient(null)
                .setPermissionInterceptor(null)
                .setWebChromeClient(null)
                .interceptUnkownUrl()
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setAgentWebUIController(null)
//                        .setMainFrameErrorView(mErrorLayoutEntity.layoutRes, mErrorLayoutEntity.reloadId)
//                        .useMiddlewareWebChrome(getMiddleWareWebChrome())
//                        .useMiddlewareWebClient(getMiddleWareWebClient())
                .createAgentWeb()//
                .ready()//
                .go("https://www.jd.com/")
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }
    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()

        super.onDestroy()
    }

}