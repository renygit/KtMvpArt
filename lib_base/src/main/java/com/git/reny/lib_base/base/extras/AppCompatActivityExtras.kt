package com.git.reny.lib_base.base.extras

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


fun AppCompatActivity.getMyDrawable(@DrawableRes drawableId:Int) : Drawable {
    return ContextCompat.getDrawable(this, drawableId)!!
}