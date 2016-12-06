package com.bb.zinglibrary.http;

import retrofit2.Callback;

/**
 * Created by zing on 2016/12/4.
 */

public interface  RequestCallback <T> {

    void onStart();
    void onResponse(T o);
    void onError(Throwable e);
    void onCompleted();
}
