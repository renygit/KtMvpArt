package com.git.reny.lib_base.base.extras.business.http

import com.git.reny.lib_base.base.GsonInstance
import com.git.reny.lib_base.base.model.BeanWrap
import retrofit2.Call
import retrofit2.Response

fun <T> Call<BeanWrap<T>>.doRequest() : T?{
    val response: Response<BeanWrap<T>> = this.execute()
    if(response.isSuccessful){//这里判断的是response的code
        response.body()?.let {
            when(it.code){//这里判断的是约定返回数据里的code 和上面不一样
                200 -> return it.newslist //看具体业务 有时会约定返回0 为正常
                //添加业务相关判断  看后端错误码约定在哪个块就写在哪个位置 有些后端出错了也返回正确
                else -> throw Throwable(it.msg)
            }
        }
        //loge("isSuccessful response: ${response.body()?.code} ： ${response.body()?.msg}")
        return response.body()?.newslist
    }else{
        val err = GsonInstance.gson.fromJson(response.errorBody()?.string(), BeanWrap::class.java)
        //loge("err>>  ${err.code} == ${response.code()}---    ${err.msg}")
        when(err.code){
            500 -> {
                throw Throwable(err.msg)
            }
            //添加业务相关判断 看后端错误码约定在哪个块就写在哪个位置
            else -> throw Throwable(err.msg)
        }
    }
}