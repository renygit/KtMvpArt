package com.git.reny.lib_imgload.core;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

public abstract class AbstractImgLoader implements ImgLoader {

    @Override
    public void gcBitmap(Bitmap bitmap) {
        if(null != bitmap && !bitmap.isRecycled()){
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
    }

    @Override
    public void cleanAll(@NonNull Context context) {
        clearDiskCache(context);
        clearMemory(context);
    }

}
