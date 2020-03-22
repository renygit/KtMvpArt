package com.git.reny.lib_imgload.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

/**
 * Created by reny on 2017/8/9.
 */

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    //主要针对V3升级到v4的用户，可以提升初始化速度，避免一些潜在错误。
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }
}
