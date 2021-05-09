package com.grank.uicommon.glide

import android.content.Context
import android.graphics.drawable.AdaptiveIconDrawable
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.drawable.ResourceDrawableDecoder
import com.bumptech.glide.module.AppGlideModule
import com.grank.uicommon.BuildConfig

@GlideModule
class GlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        if ((BuildConfig.BUILD_TYPE == "proguard" || BuildConfig.BUILD_TYPE == "debug")) {
            builder.setLogLevel(Log.INFO)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val resourceDrawableDecoder = ResourceDrawableDecoder(context)
        registry.append(
            Uri::class.java,
            AdaptiveIconDrawable::class.java,
            AdaptiveIconDecoder(resourceDrawableDecoder)
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

}