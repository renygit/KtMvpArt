package com.git.reny.lib_http;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.git.reny.lib_http.converter.GsonConverterFactory;
import com.git.reny.lib_http.core.AbstractRetrofitServiceFactory;
import com.git.reny.lib_http.core.RetrofitConfig;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/***
 *  使用方式：（Api 是网络访问接口）
 *  Api api = new RetrofitServiceFactory(config).getService(Api.class);
 *
 *  然后用RxJava  调用接口即可
 *  api.loadData()
 *  .subscribeOn((Schedulers.io()))
 *  .observeOn(AndroidSchedulers.mainThread())
 *  .subscribeWith(call);
 *
 * @param <S> 网络访问接口
 */
public class RetrofitServiceFactory<S> extends AbstractRetrofitServiceFactory<S> {

    private final String TAG = this.getClass().getSimpleName();

    private RetrofitConfig config;

    public RetrofitServiceFactory(RetrofitConfig config){
        this.config = config;
    }

    @Override
    protected Retrofit createRetrofit(Class<S> serviceClass) {
        if(config != null && config.getRetrofit() != null){
            return config.getRetrofit();
        }

        Retrofit.Builder builder =  new Retrofit.Builder()
                .baseUrl((config == null || TextUtils.isEmpty(config.getBaseUrl())) ? generateBaseUrl(serviceClass) : config.getBaseUrl())
                .client((config == null || config.getOkHttpClient() == null) ? getDefaultOkHttpClient() : config.getOkHttpClient())
                .addConverterFactory((config == null || config.getConverterFactory() == null) ? getDefaultConverterFactory() : config.getConverterFactory());

        if(config != null && config.getCallFactory() != null){
            builder.addCallAdapterFactory(config.getCallFactory());
        }

        return builder.build();
    }


    private String generateBaseUrl(Class<S> serviceClass){
        String baseUrl = null;
        if(serviceClass != null) {
            try {
                Field field = serviceClass.getField("BASE_URL");
                baseUrl = (String) field.get(serviceClass);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                Log.e(TAG, "Your Service NoSuchFieldException: BASE_URL");
            } catch (IllegalAccessException e) {
                e.getMessage();
                e.printStackTrace();
                Log.e(TAG, "IllegalAccessException");
            }
        }else {
            Log.e(TAG, "反射获取 BaseUrl 失败");
        }
        return baseUrl;
    }

    //最好由业务层自定义
    private OkHttpClient getDefaultOkHttpClient(){
        long DEFAULT_TIMEOUT = 30;//超时时间 30秒
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .hostnameVerifier(new HostnameVerifier() {
                    @SuppressLint("BadHostnameVerifier")
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;//未做验证 由业务层决定
                    }
                }).build();
    }

    private Converter.Factory getDefaultConverterFactory(){
        return GsonConverterFactory.create();
    }

    /*private CallAdapter.Factory getDefaultCallFactory(){
        return RxJava2CallAdapterFactory.create();
    }*/

}
