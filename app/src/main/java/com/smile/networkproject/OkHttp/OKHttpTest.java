package com.smile.networkproject.OkHttp;

import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author: smile .
 * date: On 2018/9/4
 */
public class OKHttpTest {

    public static OKHttpTest mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    public static OKHttpTest getInstance(Context context) {
        if (mInstance == null) {
            synchronized (OKHttpTest.class) {
                mInstance = new OKHttpTest(context);
            }
        }
        return mInstance;
    }

    public OKHttpTest(Context context) {
        File sdcache = context.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
        mHandler = new Handler();

    }

    public void getAsynHttp(String url, ResultCallback resultCallback) {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        setResult(call, resultCallback);
    }

    private void setResult(Call call, final ResultCallback resultCallback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedCallback(call.request(), e, resultCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendSuccessCallback(response, resultCallback);
            }
        });
    }

    private void sendSuccessCallback(final Response object, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    try {
                        callback.onResponse(object);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendFailedCallback(final Request object, final Exception e, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onFailure(object, e);
                }
            }
        });
    }


}
