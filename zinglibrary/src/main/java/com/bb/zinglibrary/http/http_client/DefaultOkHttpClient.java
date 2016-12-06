package com.bb.zinglibrary.http.http_client;

import android.content.Context;

import com.bb.zinglibrary.common.AndroidUtils;
import com.bb.zinglibrary.http.cookie.PersistentCookieJar;
import com.bb.zinglibrary.http.cookie.cache.SetCookieCache;
import com.bb.zinglibrary.http.cookie.persistence.SharedPrefsCookiePersistor;
import com.bb.zinglibrary.http.utils.NetworkStatusCheck;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zing on 2016/12/5.
 */

public class DefaultOkHttpClient {

    private static int maxCacheTime = 60; //超时时间


    /**
     * 默认OKHttpClient
     * 是否输出日志
     *
     * @param ableToLog
     * @return
     */
    public static OkHttpClient getDefaultOkHttpClient(boolean ableToLog) {
        return getOkHttpClient(ableToLog, null, null, null);
    }

    /**
     * 带缓存的 OKHttpClient
     *
     * @param ableToLog
     * @param androidContext
     * @return
     */
    public static OkHttpClient getCatchOkHttpClient(boolean ableToLog, Context androidContext) {
        return getOkHttpClient(ableToLog, androidContext, null, null);
    }

    /**
     * 带Header的OKHttpClient
     *
     * @param ableToLog
     * @param headerInterceptor
     * @return
     */
    public static OkHttpClient getOkHttpClientWithHeader(boolean ableToLog, Interceptor headerInterceptor) {
        return getOkHttpClient(ableToLog, null, headerInterceptor, null);
    }

    /**
     *
     */
    public static OkHttpClient getOkHttpClientWithCatchedCookie(boolean ableToLog, CookieJar cookieJar) {
        return getOkHttpClient(ableToLog, null, null, cookieJar);
    }

    /**
     * 自定义OKHttpClient
     *
     * @param ableToLog
     * @param androidContext
     * @param headerInterceptor
     * @return
     */
    public static OkHttpClient getOkHttpClient(boolean ableToLog, Context androidContext, Interceptor headerInterceptor, CookieJar serializableCookieJar) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //打印请求log日志
        if (ableToLog) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        if (androidContext != null) {
            File cacheFile = new File(AndroidUtils.getCacheDir(androidContext), "httpCache");
//        Log.d("OkHttp", "缓存目录---" + cacheFile.getAbsolutePath());
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            builder.cache(cache);
            builder.interceptors().add(getDefaultHttpCacheInterceptor(androidContext));//添加本地缓存拦截器，用来拦截本地缓存
            builder.networkInterceptors().add(getDefaultHttpCacheInterceptor(androidContext));//添加网络拦截器，用来拦截网络数据
        }

        if (headerInterceptor != null) {
            builder.addInterceptor(headerInterceptor);
        }
        if (serializableCookieJar != null) {
            builder.cookieJar(serializableCookieJar);
        }
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }


    /**
     * 默认缓存拦截器
     *
     * @param androidContext
     * @return
     */
    public static Interceptor getDefaultHttpCacheInterceptor(final Context androidContext) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetworkStatusCheck.isNetworkConnected(androidContext)) {//如果网络不可用或者设置只用网络
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
//                    Log.d("OkHttp", "网络不可用请求拦截");
                } else {//网络可用
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_NETWORK)
                            .build();
//                    Log.d("OkHttp", "网络可用请求拦截");
                }
                Response response = chain.proceed(request);
                if (NetworkStatusCheck.isNetworkConnected(androidContext)) {//如果网络可用
//                    Log.d("OkHttp", "网络可用响应拦截");
                    response = response.newBuilder()
                            //覆盖服务器响应头的Cache-Control,用我们自己的,因为服务器响应回来的可能不支持缓存
                            .header("Cache-Control", "public,max-age=" + maxCacheTime)
                            .removeHeader("Pragma")
                            .build();
                } else {
//                    Log.d("OkHttp","网络不可用响应拦截");
                    int maxStale = 60 * 60 * 24 * 30; // 断网30天，缓存失效
                    response = response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;

            }
        };
    }

    /**
     * 默认cookie持久化管理器
     *
     * @return
     */
    public static CookieJar getDefaultCookieJar(Context context) {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    }
}
