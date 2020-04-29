package com.git.reny.lib_base.base.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.git.reny.lib_base.R
import com.git.reny.lib_imgload.ImgHelper
import com.git.reny.lib_imgload.core.ImgLoader
import com.git.reny.lib_imgload.glide.BlurTransformation
import com.git.reny.lib_imgload.glide.GlideApp
import com.git.reny.lib_imgload.glide.GlideRequest
import java.util.*

class ImgUtils {
    companion object{

        val loader:ImgLoader = ImgHelper.get()

        val header = hashMapOf<String, String>(
            /*Pair<String, String>("", ""),
            Pair<String, String>("", "")*/
        )
    }
}

fun Any.display(@NonNull view: View, model:Any?, context: Context = view.context,
                header : HashMap<String, String> = ImgUtils.header,
                radius : Int = 0, isCenterCrop: Boolean = false, isBlur: Boolean = false,
                @DrawableRes placeHolder: Int= R.mipmap.img_holder,
                @DrawableRes errorHolder: Int= R.mipmap.img_holder,
                @DrawableRes fallbackHolder: Int= R.mipmap.img_holder
){
    var glideRequest: GlideRequest<Drawable> = GlideApp.with(context).load(configUrlHeader(header, model))

    glideRequest = parseConfig(context, glideRequest, radius, isCenterCrop, isBlur, placeHolder, errorHolder, fallbackHolder)

    if (view is ImageView) {
        glideRequest.into(view)
    } else { //针对LinearLayout等控件设置背景
        loadDrawableToView(glideRequest, view)
    }
}

private fun parseConfig(
    context: Context,
    glideRequest: GlideRequest<Drawable>,
    radius : Int = 0, isCenterCrop: Boolean = false, isBlur: Boolean = false,
    @DrawableRes placeHolder: Int,
    @DrawableRes errorHolder: Int,
    @DrawableRes fallbackHolder: Int
): GlideRequest<Drawable> {
    var request = glideRequest
    request = request.placeholder(placeHolder)
    request = request.error(errorHolder)
    request = request.fallback(fallbackHolder)
    if (isCenterCrop) {
        request = request.centerCrop()
    }
    if (radius > 0) {
        request = request.optionalTransform(RoundedCorners(radius))
    }
    if (isBlur) {
        try {
            request = request.optionalTransform(
                BlurTransformation(context)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //默认支持GIF  如果想自定义gif播放次数 再重写
    /*request = request.listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean = false

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            resource?.let {
                if(it is GifDrawable){
                    it.setLoopCount(GifDrawable.LOOP_FOREVER)
                }
            }
            return false
        }

    })*/
    return request
}

private fun configUrlHeader(header : HashMap<String, String>, url: Any?): Any? {
    if (header.isNotEmpty()) {
        if (url != null && url is String) {
            if (url.startsWith("http")) { //网络url(以http开头 包括https)
                val builder = LazyHeaders.Builder()
                for (key in header.keys) {
                    header[key]?.let {
                        builder.addHeader(
                            key,
                            it
                        )
                    }
                }
                return GlideUrl(url, builder.build())
            }
        }
    }
    return url
}

private fun loadDrawableToView(
    load: GlideRequest<Drawable>,
    view: View
) {
    load.into(object : CustomViewTarget<View, Drawable>(view) {
        override fun onResourceCleared(placeholder: Drawable?) {
            view.background = placeholder
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            view.background = errorDrawable
        }

        override fun onResourceReady(
            resource: Drawable,
            transition: Transition<in Drawable>?
        ) {
            view.background = resource
        }
    })
}