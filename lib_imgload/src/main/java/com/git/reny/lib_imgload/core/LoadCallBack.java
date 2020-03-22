package com.git.reny.lib_imgload.core;

import android.graphics.drawable.Drawable;

public interface LoadCallBack<T> {

    void onSuccess(T t);
    void onFailed(Drawable error);

}
