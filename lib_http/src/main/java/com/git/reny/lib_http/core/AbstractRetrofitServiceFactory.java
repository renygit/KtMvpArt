package com.git.reny.lib_http.core;

import retrofit2.Retrofit;

public abstract class AbstractRetrofitServiceFactory<S> extends AbstractServiceFactory<S> {

    @Override
    protected S createService(Class<S> serviceClass) {
        return createRetrofit(serviceClass).create(serviceClass);
    }


    protected abstract Retrofit createRetrofit(Class<S> serviceClass);


}
