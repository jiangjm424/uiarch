package com.grank.uicommon.glide

import android.graphics.*
import android.os.Build
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Util
import java.nio.ByteBuffer
import java.security.MessageDigest
import kotlin.math.max

class GlowBgAppIconTransformation(
    private val iconSize: Float,
    private val strokeSize: Float,
    private val glowRadius: Float
) : BitmapTransformation() {
    //    private val bgBitmap: Bitmap, private val cacheTag: String
    private val ID = "com.grank.uicommon.glide.GlowBgAppIconTransformation"
    private val ID_BYTES = ID.toByteArray(Key.CHARSET)

    private val defaultPaintFlag = Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG
//    private val DEFAULT_PAINT = Paint(defaultPaintFlag)

    private val circleCropPaintFlags = defaultPaintFlag or Paint.ANTI_ALIAS_FLAG
    private val circleCropGlowShapePaint = Paint(circleCropPaintFlags).apply {
        color = Color.parseColor("#997BFFE9")
        style = Paint.Style.FILL
        maskFilter = BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.OUTER)
    }
    private val circleCropShapePaint = Paint(circleCropPaintFlags).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val circleCropBitmapPaint = Paint(circleCropPaintFlags).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    override fun transform(
        pool: BitmapPool,
        inBitmap: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val outRadius = iconSize / 2 + strokeSize

        val alphaConfig = getAlphaSafeConfig(inBitmap)
        val result = pool.get(outWidth, outHeight, alphaConfig)
        result.setHasAlpha(true)

        val canvas = Canvas(result)
        // draw glow and white circle
        val centerX = outWidth / 2f
        val centerY = outWidth / 2f
        canvas.drawCircle(centerX, centerY, glowRadius, circleCropGlowShapePaint)
        canvas.drawCircle(centerX, centerY, outRadius, circleCropShapePaint)
        //draw center crop  bitmap
        drawCenterCropBitmap(pool, canvas, inBitmap, outWidth, outHeight)
        canvas.setBitmap(null)
        return result
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
        val centerY = outWidth / 2f
        val destRect = RectF(left, top, left + scaledWidth, top + scaledHeight)
        // Alpha is required for this transformation.
        val toTransform = getAlphaSafeBitmap(pool, inBitmap)
        // Draw a circle bitmap
        val count = canvas.saveLayer(destRect, circleCropShapePaint)
        canvas.drawCircle(centerX, centerY, innerRadius, circleCropShapePaint)
        val matrix = Matrix()
        matrix.setScale(maxScale, maxScale)
        canvas.translate(left, top)
        canvas.drawBitmap(toTransform, matrix, circleCropBitmapPaint)
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
        var hashCode = Util.hashCode(iconSize)
        hashCode = Util.hashCode(glowRadius, hashCode)
        hashCode = Util.hashCode(strokeSize, hashCode)
        hashCode = Util.hashCode(ID.hashCode(), hashCode)
        return hashCode
    }

    override fun equals(o: Any?): Boolean {
        if (o is GlowBgAppIconTransformation) {
            return iconSize == o.iconSize && glowRadius == o.glowRadius && strokeSize == o.strokeSize
        }
        return false
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
        val tmpBuffer = ByteBuffer.allocate(4)
        messageDigest.update(tmpBuffer.putFloat(iconSize).array())
        tmpBuffer.rewind()
        messageDigest.update(tmpBuffer.putFloat(glowRadius).array())
        tmpBuffer.rewind()
        messageDigest.update(tmpBuffer.putFloat(strokeSize).array())
    }

}