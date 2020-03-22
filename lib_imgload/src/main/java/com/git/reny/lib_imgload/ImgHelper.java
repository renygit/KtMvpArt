package com.git.reny.lib_imgload;

import com.git.reny.lib_imgload.core.ImgLoader;
import com.git.reny.lib_imgload.core.ImgLoaderConfig;
import com.git.reny.lib_imgload.core.ImgLoaderFactory;
import com.git.reny.lib_imgload.core.ImgLoaderProvider;

public class ImgHelper{

    private static ImgLoaderConfig mConfig;
    public static void initConfig(ImgLoaderConfig config){
        mConfig = config;
    }

    public static ImgLoader get(){
        return new ImgLoaderProvider().getImgLoader(mConfig);
    }

    public static ImgLoader get(ImgLoaderConfig config){
        return new ImgLoaderProvider().getImgLoader(config);
    }

    public static ImgLoader get(ImgLoaderFactory factory, ImgLoaderConfig config){
        return factory.create(config);
    }

}
