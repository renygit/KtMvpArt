package com.git.reny.lib_base.base.widget

import android.content.Context
import android.widget.ImageView
import com.git.reny.lib_imgload.ImgHelper
import com.youth.banner.loader.ImageLoader

class GlideImageLoader : ImageLoader(){
    override fun displayImage(context: Context?, path: Any, imageView: ImageView) {
        ImgHelper.get().display(imageView, path)
    }
}