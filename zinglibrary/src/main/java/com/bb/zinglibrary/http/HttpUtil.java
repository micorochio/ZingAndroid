package com.bb.zinglibrary.http;


import android.content.Context;

import com.bb.zinglibrary.http.http_client.DefaultOkHttpClient;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zing on 2016/12/4.
 */

public class HttpUtil {

    private Retrofit retrofit;

    //创建单例
    private static class SingletonHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    //获取单例
    public static HttpUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <T> void addCallBack(Observable o, final RequestCallback<T> callback) {
        Subscriber s = new Subscriber<T>() {
            @Override
            public void onStart() {
                super.onStart();
                callback.onStart();
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e);
            }

            @Override
            public void onNext(T o) {
                callback.onResponse(o);
            }

            @Override
            public void onCompleted() {
                callback.onCompleted();
            }
        };

        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


    /**
     * 创建请求对象
     *
     * @param serviceClass
     * @param client
     * @param <T>
     * @return
     */
    public <T> T buildJSONRequest(String baseUrl, Class<T> serviceClass, OkHttpClient client) {
        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        return retrofit.create(serviceClass);
    }


    public <T> T buildNormRequest(String baseUrl, Class<T> serviceClass, OkHttpClient client) {
        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        return retrofit.create(serviceClass);
    }

    public <T> T quickBuild(String baseUrl, Class<T> serviceClass, Context context) {
        retrofit = new Retrofit.Builder()
                .client(DefaultOkHttpClient.getOkHttpClient(false, context, null, null))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        return retrofit.create(serviceClass);
    }

}
