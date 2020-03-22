/*
package com.git.reny.common.utils

import java.lang.reflect.ParameterizedType

object TUtil {

    fun <T> getT(o: Any, i: Int): T? {
        try {
            return ((o.javaClass.genericSuperclass as ParameterizedType)
                .actualTypeArguments[i] as Class<T>)
                .newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

}*/
