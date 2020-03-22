package com.git.reny.lib_imgload.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.File;

import androidx.annotation.NonNull;

public interface ImgLoader {

    void display(@NonNull View view, Object model);


    //缓存到磁盘中 非内存中
    void cache(@NonNull Context context, Object model, int... widthHeight);
    //先调用cache后才能调用  加载缓存的图片
    void displayCache(@NonNull View view, Object model);
    void loadBitmap(@NonNull Context context, Object model, @NonNull final LoadCallBack<Bitmap> callBack);
    void loadDrawable(@NonNull Context context, Object model, @NonNull final LoadCallBack<Drawable> callBack);
    //下载完成 需要自己存储返回的File
    void downLoadImg(@NonNull Context context, final Object model, final LoadCallBack<File> callBack);


    void gcBitmap(Bitmap bitmap);

    void pauseRequests(@NonNull Context context);

    void resumeRequests(@NonNull Context context);

    void onTrimMemory(@NonNull Context context, int level);

    void onLowMemory(@NonNull Context context);

    void clearMemory(@NonNull Context context);

    void clearDiskCache(@NonNull final Context context);

    void cleanAll(@NonNull Context context);

}
