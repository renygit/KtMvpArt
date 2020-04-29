package com.git.reny.lib_base.base.widget

import android.content.Context
import android.widget.ImageView
import com.git.reny.lib_base.base.utils.display
import com.youth.banner.loader.ImageLoader

class GlideImageLoader : ImageLoader(){
    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        display(imageView, path, context)
        //ImgUtils.loader.display(imageView, path)
    }
}