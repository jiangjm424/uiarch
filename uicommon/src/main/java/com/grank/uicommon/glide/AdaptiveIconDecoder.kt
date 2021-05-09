package com.grank.uicommon.glide

import android.content.ContentResolver
import android.graphics.drawable.AdaptiveIconDrawable
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.drawable.ResourceDrawableDecoder

class AdaptiveIconDecoder(private val resourceDrawableDecoder: ResourceDrawableDecoder)
    : ResourceDecoder<Uri, AdaptiveIconDrawable> {
    private val adaptiveIconPkgSet = setOf("com.amazon.kindle")

    override fun handles(source: Uri, options: Options): Boolean {
        return ContentResolver.SCHEME_ANDROID_RESOURCE == source.scheme
                && source.authority != null && adaptiveIconPkgSet.contains(source.authority!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun decode(source: Uri, width: Int, height: Int, options: Options): Resource<AdaptiveIconDrawable>? {
        val resource = resourceDrawableDecoder.decode(source, width, height, options) ?: return null
        val drawable = resource.get()
        if (drawable is AdaptiveIconDrawable)
            return resource as Resource<AdaptiveIconDrawable>
        return null
    }
}