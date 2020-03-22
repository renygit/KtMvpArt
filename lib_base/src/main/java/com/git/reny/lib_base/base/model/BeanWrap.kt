package com.git.reny.lib_base.base.model

data class BeanWrap<T>(
    val code: Int,
    val msg: String,
    val newslist: T
)