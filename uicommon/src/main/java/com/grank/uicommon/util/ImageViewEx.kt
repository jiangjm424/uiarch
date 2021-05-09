package com.grank.uicommon.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * 这个文件主要是扩展了imageview使用glide加载图片的功能
 * @receiver ImageView
 * @param url String
 */
@BindingAdapter("imageUrl","errorDrawableId","placeDrawableId", requireAll = false)
fun ImageView.loadImage(url:String, errorDrawableId:Int,placeDrawableId:Int ){
    Glide.with(context)
        .load(url)
        .error(errorDrawableId)
        .placeholder(placeDrawableId)
        .into(this);
}