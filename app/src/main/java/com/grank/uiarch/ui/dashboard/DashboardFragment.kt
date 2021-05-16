package com.grank.uiarch.ui.dashboard

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grank.logger.Log
import com.grank.uiarch.R
import com.grank.uiarch.databinding.DashItemBinding
import com.grank.uiarch.databinding.FragmentDashboardBinding
import com.grank.uiarch.testvo.DemoItemImageText
import com.grank.uicommon.ui.base.AbsDataBindingFragment

class DashboardFragment : AbsDataBindingFragment<FragmentDashboardBinding>() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.fragment_dashboard

    override fun setupView(binding: FragmentDashboardBinding) {
        binding.rv.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL, false)
        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val allVisibleItemPosition  = layoutManager.findFirstCompletelyVisibleItemPosition()
                val allVisibleItem = layoutManager.findViewByPosition(allVisibleItemPosition)
                Log.i("jianga","allVisibleItemPosition $allVisibleItemPosition, itemH:${allVisibleItem?.height}, itemY:${allVisibleItem?.y}")


            }
        })
        binding.rv.addItemDecoration(object :RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom =10
            }
        })
    }

    override fun destroyView(binding: FragmentDashboardBinding) {
        binding.rv.clearOnScrollListeners()
    }
    override fun setupData(binding: FragmentDashboardBinding, lifecycleOwner: LifecycleOwner) {
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textDashboard.text = it
        })
        dashboardViewModel.item.observe(this) {
            binding.rv.adapter = MAdapter(it)
        }
    }
    class MAdapter(var datas:List<DemoItemImageText>?):RecyclerView.Adapter<VH>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val viewBinding = DashItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return VH(viewBinding)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            datas?.get(position)?.also {
                holder.bind.icon.setImageResource(it.img)
                holder.bind.desc.text = it.desc
            }
        }

        override fun getItemCount(): Int {
            return datas?.size?:0
        }
    }
    class VH(val bind:DashItemBinding):RecyclerView.ViewHolder(bind.root){}

    override fun onPageFirstComing() {
        Log.i("jiang","dash  page first coming")
    }
}