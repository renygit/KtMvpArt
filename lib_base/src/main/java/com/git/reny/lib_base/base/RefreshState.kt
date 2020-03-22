package com.git.reny.lib_base.base

import androidx.annotation.IntDef

const val STATE_REFRESH_SHOW = 0x200
const val STATE_LOAD_MORE_SHOW = 0x201

const val STATE_REFRESH_HIDE = 0x202
const val STATE_LOAD_MORE_HIDE_HAS_MORE = 0x203
const val STATE_LOAD_MORE_HIDE_NO_MORE = 0x204

@IntDef(STATE_REFRESH_SHOW, STATE_LOAD_MORE_SHOW,
    STATE_REFRESH_HIDE,  STATE_LOAD_MORE_HIDE_HAS_MORE, STATE_LOAD_MORE_HIDE_NO_MORE)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class RefreshState