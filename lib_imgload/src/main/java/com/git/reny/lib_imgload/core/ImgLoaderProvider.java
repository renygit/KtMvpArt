package com.git.reny.lib_imgload.core;

import com.git.reny.lib_imgload.glide.GlideImgLoaderFactory;

public class ImgLoaderProvider {

    private ImgLoaderFactory factory;


    public ImgLoaderProvider(){
        factory = new GlideImgLoaderFactory();
    }

    public void setFactory(ImgLoaderFactory factory) {
        this.factory = factory;
    }


    public ImgLoader getImgLoader(ImgLoaderConfig niceImgLoader){
        return factory.create(niceImgLoader);
    }



}
