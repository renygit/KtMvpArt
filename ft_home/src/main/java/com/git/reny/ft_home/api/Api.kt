package com.git.reny.ft_home.api

import com.git.reny.lib_base.config.APIConfig
import com.git.reny.lib_http.RetrofitServiceFactory
import com.git.reny.lib_http.core.RetrofitConfig

object Api {

    private val serviceFactory = RetrofitServiceFactory<ApiInterface>(RetrofitConfig.Builder().setBaseUrl(APIConfig.BASE_URL).builder())

    fun api(): ApiInterface {
        return serviceFactory.getService(ApiInterface::class.java)
    }

}