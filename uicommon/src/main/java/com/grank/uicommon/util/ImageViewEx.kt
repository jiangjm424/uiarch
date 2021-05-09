package com.grank.uicommon.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.grank.uicommon.glidex.*
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlin.math.roundToInt

/**
 * 这个文件主要是扩展了imageview使用glide加载图片的功能
 */
@BindingAdapter(value = ["imageUrl", "placeHolder", "errorImage", "circleCrop"], requireAll = false)
fun ImageView.loadImage(
    url: String?,
    holderDrawable: Drawable?=null,
    errorDrawable: Drawable?=null,
    circleCrop: Boolean = false
) {
    if (url != null) {
        when {
            circleCrop -> GlideApp.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(CircleCrop()).placeholder(holderDrawable).error(errorDrawable))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
            else -> {
                GlideApp.with(context)
                    .load(url)
                    .apply(RequestOptions().placeholder(holderDrawable).error(errorDrawable))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
        }
    } else {
        setImageDrawable(null)
        GlideApp.with(context).clear(this)
    }
}


@BindingAdapter(value = ["packageName", "circle"], requireAll = false)
fun ImageView.loadAppIcon(packageName: String?, circle: Boolean = false) {
    if (packageName != null) {
        val iconResourceId = context.packageManager.getApplicationInfo(packageName, 0).icon
        val uri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(packageName)
            .path(java.lang.String.valueOf(iconResourceId))
            .build()
        GlideApp.with(context)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply {
                if (circle)
                    apply(RequestOptions.bitmapTransform(CircleCrop()))
            }
            .into(this)
    } else {
        setImageDrawable(null)
        GlideApp.with(context).clear(this)
    }
}

@BindingAdapter(value = ["roundIconUrl", "roundForeGround", "roundIconRadius", "withCenterCrop"], requireAll = false)
fun ImageView.loadRoundImage(
    url: String?,
    roundForeGround: Drawable?,
    roundRadius: Float,
    withCenterCrop: Boolean = true
) {
    val radius = (roundRadius + 0.5f).roundToInt()
    val options: RequestOptions =
        if (withCenterCrop) {
            RequestOptions()
                .transform(CenterCrop(), RoundedCorners(radius))
        } else {
            RequestOptions()
                .transform(RoundedCorners(radius))
        }
    foreground = roundForeGround
    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(options)
        .into(this)
}

fun ImageView.loadRoundAppIcon(packageName: String?, roundRadius: Float) {
    if (packageName != null) {
        val context = context
        val iconResourceId = context.packageManager.getApplicationInfo(packageName, 0).icon
        val uri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(packageName)
            .path(java.lang.String.valueOf(iconResourceId))
            .build()
        val radius = (roundRadius + 0.5f).roundToInt()
        val options: RequestOptions = RequestOptions()
            .transform(CenterCrop(), RoundedCorners(radius))
        GlideApp.with(context)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(options)
            .into(this)
    } else {
        setImageDrawable(null)
        GlideApp.with(context).clear(this)
    }
}



fun ImageView.loadImage(
    context: Context,
    url: String?,
    option: ImageOption?=null,
    listener: RequestListener<Drawable>?=null
) {
    GlideApp.with(context)
        .load(url)
        .listener(listener)
        .placeholder(dealPlaceHolder(context, option))
        .error(dealErrorHolder(context, option))
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}


fun ImageView.loadLocalImage(
    drawable: Int,
    option: ImageOption?=null
) {
    GlideApp.with(context)
        .load(drawable)
        .placeholder(dealPlaceHolder(context, option))
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(dealErrorHolder(context, option))
        .into(this)
}

fun ImageView.loadCircle(
    context: Context,
    url: String?,
    option: ImageOption?=null
) {

    val placeHolder = dealPlaceHolder(context, option)
    val errorHolder = dealErrorHolder(context, option)
    GlideApp.with(context)
        .load(url)
        .placeholder(placeHolder)
        .error(errorHolder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .into(this)
}

/**
 * 本地图片加载成圆形
 */
fun ImageView.loadLocalCircle(
    context: Context,
    drawable: Int,
    option: ImageOption?=null
) {
    GlideApp.with(context)
        .load(drawable)
        .placeholder(dealPlaceHolder(context, option))
        .error(dealErrorHolder(context, option))
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .into(this)
}


/**
 * 加载圆形图片时，多加一个圆形外边框，外边框的宽度和颜色可以设置
 */
fun ImageView.loadCircleBorder(
    context: Context,
    url: String?,
    option: ImageOption?,
    borderWidth: Int,
    borderColor: Int
) {
    val placeHolder = dealPlaceHolder(context, option)
    val errorHolder = dealErrorHolder(context, option)
    GlideApp.with(context)
        .load(url)
        .placeholder(placeHolder)
        .error(errorHolder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .transform(
            CircleBorderTransformation(
                context,
                borderWidth,
                borderColor
            )
        )
        .into(this)
}

/**
 * 加载圆角图片，可以设置任意圆角,默认四个圆角
 */
fun ImageView.loadRoundedCorner(
    context: Context,
    url: String?,
    radius: Int,
    margin: Int,
    cornerType: RoundedCornersTransformation.CornerType=RoundedCornersTransformation.CornerType.ALL,
    imageOption: ImageOption? = null
) {
    val errorHolderDrawable =
        dealErrorHolder(context, imageOption)
    val placeHolderDrawable =
        dealPlaceHolder(context, imageOption)
    GlideApp.with(context)
        .load(url)
        .placeholder(placeHolderDrawable)
        .error(errorHolderDrawable)
        .transition(DrawableTransitionOptions.withCrossFade())
        .transform(CenterCrop(), RoundedCornersTransformation(radius, margin, cornerType))
        .into(this)
}

/**
 * 高斯模糊：网络图片，模糊半径可以设置
 */
fun ImageView.loadBlur(
    context: Context,
    url: String?,
    radius: Int,
    sampling: Int,
    imageOption: ImageOption?
) {
    val placeHolderDrawable =
        dealPlaceHolder(context, imageOption)
    val errorHolderDrawable =
        dealErrorHolder(context, imageOption)
    GlideApp.with(context)
        .load(url)
        .placeholder(placeHolderDrawable)
        .error(errorHolderDrawable)
        .transition(DrawableTransitionOptions.withCrossFade())
        .transform(BlurTransformation(radius, sampling))
        .into(this)
}

/**
 * 高斯模糊：本地图片，模糊半径可以设置
 */
fun ImageView.loadBlur(
    context: Context,
    drawableId: Int,
    radius: Int,
    sampling: Int,
    imageOption: ImageOption?
) {
    val placeHolderDrawable =
        dealPlaceHolder(context, imageOption)
    val errorHolderDrawable =
        dealErrorHolder(context, imageOption)
    GlideApp.with(context)
        .load(drawableId)
        .placeholder(placeHolderDrawable)
        .error(errorHolderDrawable)
        .transition(DrawableTransitionOptions.withCrossFade())
        .transform(BlurTransformation(radius, sampling))
        .into(this)
}

/**
 * 加载带有发光背景的appIcon
 * @receiver ImageView
 * @param packageName String?
 * @param iconSize Float
 * @param strokeSize Float
 * @param glowRadius Float
 */
fun ImageView.loadGlowBgAppIcon(
    packageName: String?,
    iconSize: Float,
    strokeSize: Float,
    glowRadius: Float
) {
    if (packageName != null) {
        val context = context
        val iconResourceId = context.packageManager.getApplicationInfo(packageName, 0).icon
        val uri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(packageName)
            .path(java.lang.String.valueOf(iconResourceId))
            .build()
        GlideApp.with(context)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(GlowBgAppIconTransformation(iconSize, strokeSize, glowRadius))
            .into(this)
    } else {
        setImageDrawable(null)
        GlideApp.with(context).clear(this)
    }
}

/**
 * 加载高斯模糊背景的appIcon
 * @receiver ImageView
 * @param packageName String?
 * @param maskDrawableId Int
 * @param iconSize Float
 * @param blurRadius Float
 */
fun ImageView.loadBlurBgAppIcon(
    packageName: String?,
    maskDrawableId: Int,
    iconSize: Float,
    blurRadius: Float
) {
    if (packageName != null) {
        val iconResourceId = context.packageManager.getApplicationInfo(packageName, 0).icon
        val uri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(packageName)
            .path(java.lang.String.valueOf(iconResourceId))
            .build()
        GlideApp.with(context)
            .load(uri)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(BlurBgAppIconTransformation(context.applicationContext, maskDrawableId, iconSize, blurRadius))
            .into(this)
    } else {
        setImageDrawable(null)
        GlideApp.with(context).clear(this)
    }
}


/**
 * 通过ResId转换为drawable
 *
 * @param context
 * @param option
 * @return
 */
private fun dealErrorHolder(
    context: Context,
    option: ImageOption?
): Drawable? {
    var errorHolderDrawable: Drawable?
    if (null != option) {
        errorHolderDrawable = option.getErrorHolderDrawable(context.resources)
        if (null == errorHolderDrawable) {
            errorHolderDrawable = ContextCompat.getDrawable(
                context,
                option.errorHolder
            )
        }
    } else {
        errorHolderDrawable = mDefaultOption.getErrorHolderDrawable(context.resources)
    }
    return errorHolderDrawable
}

/**
 * 通过ResId转换为drawable
 *
 * @param context
 * @param option
 * @return
 */
private fun dealPlaceHolder(
    context: Context,
    option: ImageOption?
): Drawable? {
    var placeHolderDrawable: Drawable?
    if (null != option) {
        placeHolderDrawable = option.getPlaceHolderDrawable(context.resources)
        if (null == placeHolderDrawable) {
            placeHolderDrawable = ContextCompat.getDrawable(
                context,
                option.placeHolder
            )
        }
    } else {
        placeHolderDrawable = mDefaultOption.getPlaceHolderDrawable(context.resources)
    }
    return placeHolderDrawable
}

private val mDefaultOption = ImageOption.Builder()
    .setErrorHolder(ImageOption.DEFAULT_ERROR_HOLDER)
    .setPlaceHolder(ImageOption.DEFAULT_PLACE_HOLDER)
    .build()