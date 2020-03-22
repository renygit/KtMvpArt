package com.git.reny.lib_imgload.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.git.reny.lib_imgload.core.AbstractImgLoader;
import com.git.reny.lib_imgload.core.ImgLoaderConfig;
import com.git.reny.lib_imgload.core.LoadCallBack;

import java.io.File;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GlideImgLoader extends AbstractImgLoader {

    private ImgLoaderConfig config;

    GlideImgLoader(ImgLoaderConfig config){
        this.config = config;
    }


    @Override
    public void display(@NonNull View view, Object model) {
        GlideRequests glideRequests = GlideApp.with(view);
        if(view instanceof ImageView){
            ImageView imageView = (ImageView) view;
            if(config == null){
                glideRequests.load(model).into(imageView);
            }else {
                GlideRequest<Drawable> glideRequest = glideRequests.load(configUrlHeader(model));
                parseConfig(imageView.getContext(), glideRequest).into(imageView);
            }
        }else {//针对LinearLayout等控件设置背景
            if(config == null){
                loadDrawableToView(glideRequests.asDrawable().load(model), view);
            }else {
                GlideRequest<Drawable> glideRequest = glideRequests.load(configUrlHeader(model));
                loadDrawableToView(parseConfig(view.getContext(), glideRequest), view);
            }
        }
    }

    private void loadDrawableToView(GlideRequest<Drawable> load, View view) {
        load.into(new CustomViewTarget<View, Drawable>(view) {
            @Override
            protected void onResourceCleared(@Nullable Drawable placeholder) {
                view.setBackground(placeholder);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                view.setBackground(errorDrawable);
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                view.setBackground(resource);
            }
        });
    }

    private GlideRequest<Drawable> parseConfig(Context context, GlideRequest<Drawable> glideRequest) {
        if(config.getPlace_holder() != null){
            glideRequest = glideRequest.placeholder(config.getPlace_holder());
        }
        if(config.getError_holder() != null){
            glideRequest = glideRequest.error(config.getError_holder());
        }
        if(config.getFallback_holder() != null){
            glideRequest = glideRequest.fallback(config.getFallback_holder());
        }

        if(config.isCenterCrop()) {
            glideRequest = glideRequest.centerCrop();
        }
        if(config.getRadius() > 0){
            glideRequest = glideRequest.optionalTransform(new RoundedCorners(config.getRadius()));
        }
        if(config.isBlur()) {
            try {
                glideRequest = glideRequest.optionalTransform(new BlurTransformation(context));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return glideRequest;
    }

    private Object configUrlHeader(Object url) {
        if(config != null && config.getHeader() != null && config.getHeader().size() > 0) {
            if (url instanceof String) {
                if (!TextUtils.isEmpty((String) url) && ((String) url).startsWith("http")) {//网络url(以http开头 包括https)
                    LazyHeaders.Builder builder = new LazyHeaders.Builder();
                    for (String key : config.getHeader().keySet()) {
                        builder.addHeader(key, Objects.requireNonNull(config.getHeader().get(key)));
                    }
                    return new GlideUrl((String) url, builder.build());
                }
            }
        }
        return url;
    }


    //缓存到磁盘中 非内存中
    @Override
    public void cache(@NonNull Context context, Object model, int... widthHeight) {
        GlideRequests glideRequests = GlideApp.with(context);
        model = configUrlHeader(model);
        try {
            if(widthHeight.length == 0) {
                glideRequests.download(model).preload();
            }else if(widthHeight.length == 1){
                glideRequests.download(model).preload(widthHeight[0], widthHeight[0]);
            }else{
                glideRequests.download(model).preload(widthHeight[0], widthHeight[1]);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void displayCache(@NonNull View view, Object model) {
        try {
            GlideRequests glideRequests = GlideApp.with(view);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                if(config == null){
                    glideRequests.load(model).onlyRetrieveFromCache(true).into(imageView);
                }else {
                    GlideRequest<Drawable> glideRequest = glideRequests.load(configUrlHeader(model));
                    parseConfig(imageView.getContext(), glideRequest).onlyRetrieveFromCache(true).into(imageView);
                }
            } else {
                if(config == null){
                    loadDrawableToView(glideRequests.asDrawable().load(model).onlyRetrieveFromCache(true), view);
                }else {
                    GlideRequest<Drawable> glideRequest = glideRequests.load(configUrlHeader(model));
                    loadDrawableToView(parseConfig(view.getContext(), glideRequest).onlyRetrieveFromCache(true), view);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            //调用这个方法要求先调用cache  如果缓存被清理了 可能出现问题 这里兜底方案
            display(view, model);
        }
    }

    @Override
    public void loadBitmap(@NonNull Context context, Object model, @NonNull final LoadCallBack<Bitmap> callBack) {
        try {
            GlideRequests glideRequests = GlideApp.with(context);
            model = configUrlHeader(model);
            glideRequests.asBitmap().load(model).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                    callBack.onSuccess(resource);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    callBack.onFailed(errorDrawable);
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void loadDrawable(@NonNull Context context, Object model, @NonNull final LoadCallBack<Drawable> callBack) {
        try {
            GlideRequests glideRequests = GlideApp.with(context);
            model = configUrlHeader(model);
            glideRequests.asDrawable().load(model).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
                    callBack.onSuccess(resource);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    callBack.onFailed(errorDrawable);
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void downLoadImg(@NonNull Context context, Object model, final LoadCallBack<File> callBack) {
        try {
            final GlideRequests glideRequests = GlideApp.with(context);
            final Object finalModel = configUrlHeader(model);
            GlideThreadTask.addTask(new Runnable() {
                @Override
                public void run() {
                    glideRequests.download(finalModel).into(new SimpleTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
                            callBack.onSuccess(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            callBack.onFailed(errorDrawable);
                        }
                    });
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void pauseRequests(@NonNull Context context) {
        GlideApp.with(context).pauseRequests();
    }

    @Override
    public void resumeRequests(@NonNull Context context) {
        GlideApp.with(context).resumeRequests();
    }

    @Override
    public void onTrimMemory(@NonNull Context context, int level) {
        Glide.get(context).trimMemory(level);
    }

    @Override
    public void onLowMemory(@NonNull Context context) {
        Glide.get(context).onLowMemory();
    }

    @Override
    public void clearMemory(@NonNull Context context) {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache(@NonNull final Context context) {
        GlideThreadTask.addTask(new Runnable() {
            @Override
            public void run() {
                // This method must be called on a background thread.
                Glide.get(context).clearDiskCache();
            }
        });
    }

}
