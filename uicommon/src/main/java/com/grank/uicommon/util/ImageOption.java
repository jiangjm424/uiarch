package com.grank.uicommon.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

import com.grank.uicommon.R;

/**
 * 图片加载相关设置选项
 */
public class ImageOption {

    public static final int DEFAULT_ERROR_HOLDER = R.drawable.icon_default;
    public static final int DEFAULT_PLACE_HOLDER = R.drawable.icon_default;

    private final int mPlaceHolderResId;
    private final int mErrorHolderResId;
    private final Drawable mPlaceHolderDrawable;
    private final Drawable mErrorHolderDrawable;

    public ImageOption(Builder builder) {

        mPlaceHolderResId = builder.placeHolderResId;
        mErrorHolderResId = builder.errorHolderResId;
        mPlaceHolderDrawable = builder.placeHolderDrawable;
        mErrorHolderDrawable = builder.errorHolderDrawable;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public int getErrorHolder() {
        return this.mErrorHolderResId != 0 ? this.mErrorHolderResId : DEFAULT_ERROR_HOLDER;
    }

    public int getPlaceHolder() {
        return this.mPlaceHolderResId != 0 ? this.mPlaceHolderResId : DEFAULT_PLACE_HOLDER;
    }

    public Drawable getPlaceHolderDrawable(Resources res) {
        return this.mPlaceHolderDrawable != null ? this.mPlaceHolderDrawable : res.getDrawable(this.getPlaceHolder());
    }

    public Drawable getErrorHolderDrawable(Resources res) {
        return this.mErrorHolderDrawable != null ? this.mErrorHolderDrawable : res.getDrawable(this.getErrorHolder());
    }

    public static final class Builder {
        int placeHolderResId;
        int errorHolderResId;
        Drawable placeHolderDrawable;
        Drawable errorHolderDrawable;

        public Builder() {
            initDefault(null);
        }

        public Builder(Resources res) {
            initDefault(res);
        }

        public Builder(ImageOption option) {
            this.placeHolderResId = option.mPlaceHolderResId;
            this.errorHolderResId = option.mErrorHolderResId;
            this.placeHolderDrawable = option.mPlaceHolderDrawable;
            this.errorHolderDrawable = option.mErrorHolderDrawable;
        }

        /**
         * 初始化默认数据
         *
         * @param res
         */
        private void initDefault(Resources res) {
            //占位图
            this.placeHolderResId = DEFAULT_PLACE_HOLDER;
            //错误图
            this.errorHolderResId = DEFAULT_ERROR_HOLDER;
            if (null != res) {
                //占位图
                this.placeHolderDrawable = res.getDrawable(DEFAULT_PLACE_HOLDER);
                //错误图
                this.errorHolderDrawable = res.getDrawable(DEFAULT_ERROR_HOLDER);
            }

        }

        public Builder setPlaceHolder(@DrawableRes int resId) {
            this.placeHolderResId = resId;
            return this;
        }

        public Builder setErrorHolder(@DrawableRes int resId) {
            this.errorHolderResId = resId;
            return this;
        }

        public Builder setPlaceDrawableHolder(Drawable placeHolder) {
            this.placeHolderDrawable = placeHolder;
            return this;
        }

        public Builder setErrorDrawableHolder(Drawable errorHolder) {
            this.errorHolderDrawable = errorHolder;
            return this;
        }

        public ImageOption build() {
            return new ImageOption(this);
        }

    }

}
