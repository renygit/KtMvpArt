package com.git.reny.lib_imgload.core;

import android.graphics.drawable.Drawable;

import java.util.Map;

public class ImgLoaderConfig {

    private Map<String, String> header;
    private Drawable place_holder;//尚未加载时显示的图片
    private Drawable error_holder;//加载失败时显示（图片加载失败）
    private Drawable fallback_holder;//请求的url/model为 null 时展示(图片不存在)
    private int radius;//图片圆角
    private boolean isCenterCrop;
    private boolean isBlur;//是否糊糊图片

    public ImgLoaderConfig(Builder builder) {
        this.header = builder.header;
        this.place_holder = builder.place_holder;
        this.error_holder = builder.error_holder;
        this.fallback_holder = builder.fallback_holder;
        this.radius = builder.radius;
        this.isCenterCrop = builder.isCenterCrop;
        this.isBlur = builder.isBlur;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public Drawable getPlace_holder() {
        return place_holder;
    }

    public Drawable getError_holder() {
        return error_holder;
    }

    public Drawable getFallback_holder() {
        return fallback_holder;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isCenterCrop() {
        return isCenterCrop;
    }

    public boolean isBlur() {
        return isBlur;
    }

    public static class Builder{

        private Map<String, String> header;
        private Drawable place_holder;//尚未加载时显示的图片
        private Drawable error_holder;//加载失败时显示（图片加载失败）
        private Drawable fallback_holder;//请求的url/model为 null 时展示(图片不存在)
        private int radius;//图片圆角
        private boolean isCenterCrop;
        private boolean isBlur;//是否糊糊图片

        public Builder setHeader(Map<String, String> header) {
            this.header = header;
            return this;
        }

        public Builder setPlace_holder(Drawable place_holder) {
            this.place_holder = place_holder;
            return this;
        }

        public Builder setError_holder(Drawable error_holder) {
            this.error_holder = error_holder;
            return this;
        }

        public Builder setFallback_holder(Drawable fallback_holder) {
            this.fallback_holder = fallback_holder;
            return this;
        }

        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder setCenterCrop(boolean centerCrop) {
            isCenterCrop = centerCrop;
            return this;
        }

        public Builder setBlur(boolean blur) {
            isBlur = blur;
            return this;
        }


        public ImgLoaderConfig builder(){
            return new ImgLoaderConfig(this);
        }

    }

}
