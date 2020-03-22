package com.git.reny.ft_home.api

import com.git.reny.ft_home.model.Girl
import com.git.reny.lib_base.base.model.BeanWrap
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {


    @GET("meinv/index?key=2d714f704989edca7000d69d64f74a31")
    fun get_imgs(@Query("page") page: Int, @Query("num") num: Int = 20,  @Query("word") word: String? = null): Call<BeanWrap<List<Girl>>>


}