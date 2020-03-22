package com.git.reny.lib_imgload.glide;

import com.git.reny.lib_imgload.core.ImgLoader;
import com.git.reny.lib_imgload.core.ImgLoaderFactory;
import com.git.reny.lib_imgload.core.ImgLoaderConfig;

public class GlideImgLoaderFactory implements ImgLoaderFactory {

    @Override
    public ImgLoader create(ImgLoaderConfig imgLoader) {
        return new GlideImgLoader(imgLoader);
    }

}
