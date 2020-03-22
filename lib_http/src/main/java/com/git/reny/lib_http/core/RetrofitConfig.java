package com.git.reny.lib_http.core;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class RetrofitConfig {

    private Retrofit retrofit;

    private String baseUrl;
    private OkHttpClient okHttpClient;
    private Converter.Factory converterFactory;
    private CallAdapter.Factory callFactory;

    public RetrofitConfig(Builder builder) {
        this.retrofit = builder.retrofit;
        this.baseUrl = builder.baseUrl;
        this.okHttpClient = builder.okHttpClient;
        this.converterFactory = builder.converterFactory;
        this.callFactory = builder.callFactory;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Converter.Factory getConverterFactory() {
        return converterFactory;
    }

    public CallAdapter.Factory getCallFactory() {
        return callFactory;
    }

    public static class Builder{

        private Retrofit retrofit;

        private String baseUrl;
        private OkHttpClient okHttpClient;
        private Converter.Factory converterFactory;
        private CallAdapter.Factory callFactory;

        public Builder setRetrofit(Retrofit retrofit) {
            this.retrofit = retrofit;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder setConverterFactory(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public Builder setCallFactory(CallAdapter.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public RetrofitConfig builder(){
            return new RetrofitConfig(this);
        }

    }

}
