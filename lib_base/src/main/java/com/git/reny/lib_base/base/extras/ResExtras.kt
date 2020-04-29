package com.git.reny.lib_base.base.extras

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat


fun Any.getDrawable(@DrawableRes drawableId:Int, context: Context) : Drawable {
    return ContextCompat.getDrawable(context, drawableId)!!
}

fun Any.getColor(@ColorRes colorId:Int, context: Context) : Int {
    return ContextCompat.getColor(context, colorId)
}