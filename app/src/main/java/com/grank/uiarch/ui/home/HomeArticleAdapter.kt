package com.grank.uiarch.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grank.datacenter.model.Article
import com.grank.logger.Log
import com.grank.uiarch.R
import com.grank.uiarch.databinding.HomeListItemBinding
import com.grank.uicommon.util.loadImage
import javax.inject.Inject


class HomeArticleAdapter @Inject constructor() : RecyclerView.Adapter<HomeArticleAdapter.VH>() {

    private val articles = mutableListOf<Article>()


    fun add(list: List<Article>,clear:Boolean = false) {
        if (clear) {
            articles.clear()
        }
        val s = articles.size
        articles.addAll(articles.size, list)
        notifyItemRangeInserted(s, articles.size)
    }

    class VH(val bind: HomeListItemBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding =
            HomeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = articles?.get(position)
        Log.i("jiang", "bind item:$position, item:${item?.chapterName}")
        item?.let {
            holder.bind.homeItemTitle.text = it.title
            holder.bind.homeItemAuthor.text = it.author
            holder.bind.homeItemType.text = it.chapterName
            holder.bind.homeItemDate.text = it.niceDate
            val collect =
                if (it.collect) R.drawable.ic_action_like else R.drawable.ic_action_no_like
            holder.bind.homeItemLike.loadImage(collect)
        }
    }

    override fun getItemCount(): Int {
        return articles?.size ?: 0
    }
}