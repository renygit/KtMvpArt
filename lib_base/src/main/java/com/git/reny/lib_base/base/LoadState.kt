package com.git.reny.lib_base.base

import androidx.annotation.IntDef

const val STATE_LOADING = 0x100
const val STATE_ERROR = 0x101
const val STATE_NO_NETWORK = 0x102
const val STATE_EMPTY = 0x103
const val STATE_CONTENT = 0x104

@IntDef(STATE_LOADING, STATE_ERROR, STATE_NO_NETWORK, STATE_EMPTY, STATE_CONTENT)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class LoadState