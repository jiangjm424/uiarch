package com.grank.uicommon.glidex

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.security.MessageDigest
import kotlin.math.max

class BlurBgAppIconTransformation(
    private val appContext: Context,
    private val maskDrawableId: Int,
    private val iconSize: Float,
    private val blurRadius: Float
) : BitmapTransformation() {
    //    private val bgBitmap: Bitmap, private val cacheTag: String
    private val ID = "com.grank.uicommon.glide.BlurBgAppIconTransformation"
    private val ID_BYTES = ID.toByteArray(Key.CHARSET)

    private var maskDrawable: Drawable? = null
    private val renderScript = RenderScript.create(appContext.applicationContext)
    private val downSampler = 4
    private val wantBlurRadius = blurRadius
    private var downBlurRadius = blurRadius / downSampler
    private val blurScript =
        ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript)).apply {
            // 设置渲染的模糊程度, 25f是最大模糊度
            if (downBlurRadius > 25f)
                downBlurRadius = 25f
            setRadius(downBlurRadius)
        }

    private val defaultPaintFlag = Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG
    private val defaultPaint = Paint(defaultPaintFlag)
    private val circleCropPaintFlags = defaultPaintFlag or Paint.ANTI_ALIAS_FLAG
    private val circleCropGlowShapePaint = Paint(circleCropPaintFlags).apply {
        color = Color.parseColor("#997BFFE9")
        style = Paint.Style.FILL
//        maskFilter = BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.OUTER)
    }
    private val circleCropShapePaint = Paint(circleCropPaintFlags).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val circleCropBitmapPaint = Paint(circleCropPaintFlags).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    private val tmpMatrix = Matrix()
    private val srcRect = Rect()
    private val dstRect = Rect()

    override fun transform(
        pool: BitmapPool,
        inBitmap: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val alphaConfig = getAlphaSafeConfig(inBitmap)
        val result = pool.get(outWidth, outHeight, alphaConfig)
        result.setHasAlpha(true)
        val canvas = Canvas(result)
        drawBlurBitmap(canvas, pool, inBitmap, outHeight, outWidth)
        drawMaskDrawable(canvas, outWidth, outHeight)
        drawCenterCropBitmap(pool, canvas, inBitmap, outWidth, outHeight)
        canvas.setBitmap(null)
        return result
    }

    private fun drawMaskDrawable(canvas: Canvas, outWidth: Int, outHeight: Int) {
        val maskDrawable2 = this.maskDrawable ?: appContext.getDrawable(maskDrawableId)!!
        if (this.maskDrawable == null)
            this.maskDrawable = maskDrawable2
        maskDrawable2.setBounds(0, 0, outWidth, outHeight)
        maskDrawable2.draw(canvas)
    }

    private fun drawBlurBitmap(
        canvas: Canvas,
        pool: BitmapPool,
        inBitmap: Bitmap,
        outHeight: Int,
        outWidth: Int
    ) {
        val toBlurBitmap = prepareBlurBitmap(pool, inBitmap, outWidth, outHeight)
        val blurBitmap = blurBitmap(pool, toBlurBitmap)
        pool.put(toBlurBitmap)
        srcRect.set(0, 0, blurBitmap.width, blurBitmap.height)
        dstRect.set(0, 0, outWidth, outHeight)
        canvas.drawBitmap(blurBitmap, srcRect, dstRect, defaultPaint)
        pool.put(blurBitmap)
    }

    private fun prepareBlurBitmap(
        pool: BitmapPool,
        inBitmap: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val alphaConfig = getAlphaSafeConfig(inBitmap)
        val toBlurBitmap = pool.get(outWidth, outHeight, alphaConfig)
        toBlurBitmap.setHasAlpha(true)
        val canvas = Canvas(toBlurBitmap)
        val widthScale = outWidth * 1f / inBitmap.width
        val heightScale = outHeight * 1f / inBitmap.height
        val maxScale = max(widthScale, heightScale)
        val targetWidth = maxScale * inBitmap.width
        val targetHeight = maxScale * inBitmap.height
        val translateX = (outWidth - targetWidth) / 2
        val translateY = (outHeight - targetHeight) / 2
        tmpMatrix.postScale(maxScale, maxScale)
        tmpMatrix.postTranslate(translateX, translateY)
        canvas.drawBitmap(inBitmap, tmpMatrix, defaultPaint)
        tmpMatrix.reset()
        canvas.setBitmap(null)
        return toBlurBitmap
    }

    private fun blurBitmap(pool: BitmapPool, toBlurBitmap: Bitmap): Bitmap {
        val scaledWidth = toBlurBitmap.width / downSampler
        val scaledHeight = toBlurBitmap.height / downSampler
        val alphaConfig = getAlphaSafeConfig(toBlurBitmap)
        val toBlurRealBitmap = pool.get(scaledWidth, scaledHeight, alphaConfig)
        val blurBitmap = pool.get(scaledWidth, scaledHeight, alphaConfig)

        // 准备好图片
        val canvas = Canvas(toBlurRealBitmap)
        canvas.scale(scaledWidth * 1f / toBlurBitmap.width, scaledHeight * 1f / toBlurBitmap.height)
        canvas.drawBitmap(toBlurBitmap, tmpMatrix, defaultPaint)

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        val tmpIn = Allocation.createFromBitmap(renderScript, toBlurRealBitmap)
        val tmpOut: Allocation = Allocation.createFromBitmap(renderScript, blurBitmap)
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn)
        blurScript.forEach(tmpOut)
        // 将输出数据保存到输出内存中
        tmpOut.copyTo(blurBitmap)
        canvas.setBitmap(null)
        pool.put(toBlurRealBitmap)
        return blurBitmap
    }

    private fun drawCenterCropBitmap(
        pool: BitmapPool,
        canvas: Canvas,
        inBitmap: Bitmap,
        outWidth: Int,
        outHeight: Int
    ) {
        val innerRadius = iconSize / 2
        val scaleX = iconSize / inBitmap.width
        val scaleY = iconSize / inBitmap.height
        val maxScale = max(scaleX, scaleY)
        val scaledWidth = maxScale * inBitmap.width
        val scaledHeight = maxScale * inBitmap.height
        val left = (outWidth - scaledWidth) / 2f
        val top = (outHeight - scaledHeight) / 2f
        val centerX = outWidth / 2f
        val centerY = outHeight / 2f
        val destRect = RectF(left, top, left + scaledWidth, top + scaledHeight)
        // Alpha is required for this transformation.
        val toTransform = getAlphaSafeBitmap(pool, inBitmap)
        // Draw a circle bitmap
        val count = canvas.saveLayer(destRect, circleCropShapePaint)
        canvas.drawCircle(centerX, centerY, innerRadius, circleCropShapePaint)
        tmpMatrix.reset()
        tmpMatrix.setScale(maxScale, maxScale)
        canvas.translate(left, top)
        canvas.drawBitmap(toTransform, tmpMatrix, circleCropBitmapPaint)
        canvas.restoreToCount(count)
    }

    private fun getAlphaSafeConfig(inBitmap: Bitmap): Bitmap.Config {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Bitmap.Config.RGBA_F16 == inBitmap.config) { // NOPMD
                return Bitmap.Config.RGBA_F16
            }
        }
        return Bitmap.Config.ARGB_8888
    }

    private fun getAlphaSafeBitmap(pool: BitmapPool, maybeAlphaSafe: Bitmap): Bitmap {
        val safeConfig = getAlphaSafeConfig(maybeAlphaSafe)
        if (safeConfig == maybeAlphaSafe.config) {
            return maybeAlphaSafe
        }
        val argbBitmap = pool[maybeAlphaSafe.width, maybeAlphaSafe.height, safeConfig]
        Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0f, 0f, null /*paint*/)

        // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
        // when we're finished with it.
        return argbBitmap
    }

    override fun hashCode(): Int {
        var hashCode = Util.hashCode(maskDrawableId)
        hashCode = Util.hashCode(iconSize, hashCode)
        hashCode = Util.hashCode(blurRadius, hashCode)
        hashCode = Util.hashCode(ID.hashCode(), hashCode)
        return hashCode
    }

    override fun equals(o: Any?): Boolean {
        if (o is BlurBgAppIconTransformation) {
            return maskDrawableId == o.maskDrawableId && iconSize == o.iconSize && blurRadius == o.blurRadius
        }
        return false
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
        val tmpBuffer = ByteBuffer.allocate(4)
        messageDigest.update(tmpBuffer.putInt(maskDrawableId).array())
        tmpBuffer.rewind()
        messageDigest.update(tmpBuffer.putFloat(iconSize).array())
        tmpBuffer.rewind()
        messageDigest.update(tmpBuffer.putFloat(blurRadius).array())
    }

}