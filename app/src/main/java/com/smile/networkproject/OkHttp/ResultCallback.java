package com.smile.networkproject.OkHttp;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * author: smile .
 * date: On 2018/9/4
 */
public abstract class ResultCallback {

    public abstract void onResponse(Response response) throws IOException;

    public abstract void onFailure(Request request, Exception e);
}
