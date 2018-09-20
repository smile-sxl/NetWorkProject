package com.smile.networkproject.Retrofit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.smile.networkproject.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * author: smile .
 * date: On 2018/9/2
 */
public class RetrofitTestActivity extends AppCompatActivity {

    private TextView tvData;
    private ImageView ivData;
    private NetworkImageView nivData;
    private static final String TAG = "RetrofitTestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        tvData = (TextView) findViewById(R.id.tvData);
        ivData = (ImageView) findViewById(R.id.ivData);
        nivData = (NetworkImageView) findViewById(R.id.nivData);

        ivData.setVisibility(View.GONE);
        nivData.setVisibility(View.GONE);

        sendAsyncPostForBody();

    }

    public interface meituService {
        @GET("meituApi?page=1")
        Call<Translation> getCall();
    }

    public interface toutiaoServiceForPath {
        @GET("{path}?type=&key=c93dce5519eac6146c154661983023f7")
        Call<TopNew> getCall(@Path("path") String path);
    }

    public interface toutiaoServiceForQueryMap {
        @GET("index")
        Call<TopNew> getCall(@QueryMap Map<String, String> options);
    }

    public interface toutiaoServicePost {
        @FormUrlEncoded
        @POST("index")
        Call<TopNew> getCall(@Field("key") String key);
    }

    public interface toutiaoServicePostForBody {
        @POST("index")
        Call<TopNew> getCall(@Body Key key);
    }

    public class Key {
        private String key;

        public Key( String key) {
            this.key = key;
        }
    }

    /**
     * 异步get请求
     */
    private void sendAsyncGet() {
        String url = "https://www.apiopen.top/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        meituService meitu = retrofit.create(meituService.class);
        Call<Translation> call = meitu.getCall();
        call.enqueue(new Callback<Translation>() {
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                String type = response.body().getData().get(0).getType();
                Toast.makeText(RetrofitTestActivity.this, type, Toast.LENGTH_SHORT).show();
                tvData.setText(response.body().getData().get(0).getUrl());
            }

            @Override
            public void onFailure(Call<Translation> call, Throwable t) {
                Log.e(TAG, "onFailure: ----------");
            }
        });
    }

    /**
     * 同步get请求
     */
    private void sendGet() {
        String url = "https://www.apiopen.top/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        meituService meitu = retrofit.create(meituService.class);
        Call<Translation> call = meitu.getCall();
        try {
            Log.e(TAG, "sendGet: --------" + call.execute().body().getCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步get请求
     */
    private void sendAsyncGetForPath() {
        String url = "http://v.juhe.cn/toutiao/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        toutiaoServiceForPath meitu = retrofit.create(toutiaoServiceForPath.class);
        Call<TopNew> call = meitu.getCall("index");
        Log.e(TAG, "sendAsyncGetForPath:------- " + call.request());
        call.enqueue(new Callback<TopNew>() {
            @Override
            public void onResponse(Call<TopNew> call, Response<TopNew> response) {
                int code = response.body().getError_code();
                Toast.makeText(RetrofitTestActivity.this, code + "", Toast.LENGTH_SHORT).show();
                tvData.setText(code + "");
            }

            @Override
            public void onFailure(Call<TopNew> call, Throwable t) {
                Log.e(TAG, "onFailure: ----------");
            }
        });
    }

    /**
     * 异步get请求
     */
    private void sendAsyncGetForQueryMap() {
        String url = "http://v.juhe.cn/toutiao/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        toutiaoServiceForQueryMap meitu = retrofit.create(toutiaoServiceForQueryMap.class);
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", "shishang");
        map.put("key", "c93dce5519eac6146c154661983023f7");
        Call<TopNew> call = meitu.getCall(map);
        Log.e(TAG, "sendAsyncGetForPath:------- " + call.request());
        call.enqueue(new Callback<TopNew>() {
            @Override
            public void onResponse(Call<TopNew> call, Response<TopNew> response) {
                int code = response.body().getError_code();
                Toast.makeText(RetrofitTestActivity.this, code + "", Toast.LENGTH_SHORT).show();
                tvData.setText(code + "");
            }

            @Override
            public void onFailure(Call<TopNew> call, Throwable t) {
                Log.e(TAG, "onFailure: ----------");
            }
        });
    }

    /**
     * 异步Post请求
     */
    private void sendAsyncPost() {
        String url = "http://v.juhe.cn/toutiao/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        toutiaoServicePost meitu = retrofit.create(toutiaoServicePost.class);
        Call<TopNew> call = meitu.getCall("c93dce5519eac6146c154661983023f7");
        Log.e(TAG, "sendAsyncGetForPath:------- " + call.request());
        call.enqueue(new Callback<TopNew>() {
            @Override
            public void onResponse(Call<TopNew> call, Response<TopNew> response) {
                int code = response.body().getError_code();
                Toast.makeText(RetrofitTestActivity.this, code + "", Toast.LENGTH_SHORT).show();
                tvData.setText(code + "");
            }

            @Override
            public void onFailure(Call<TopNew> call, Throwable t) {
                Log.e(TAG, "onFailure: ----------");
            }
        });
    }


    /**
     * 异步Post请求
     */
    private void sendAsyncPostForBody() {
        String url = "http://v.juhe.cn/toutiao/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        toutiaoServicePostForBody meitu = retrofit.create(toutiaoServicePostForBody.class);
        Key key = new Key("c93dce5519eac6146c154661983023f7");
        Call<TopNew> call = meitu.getCall(key);
        Log.e(TAG, "sendAsyncGetForPath:request------- " + call.request());

        call.enqueue(new Callback<TopNew>() {
            @Override
            public void onResponse(Call<TopNew> call, Response<TopNew> response) {
                int code = response.body().getError_code();
                Toast.makeText(RetrofitTestActivity.this, code + "", Toast.LENGTH_SHORT).show();
                tvData.setText(code + "");
            }

            @Override
            public void onFailure(Call<TopNew> call, Throwable t) {
                Log.e(TAG, "onFailure: ----------");
            }
        });
    }
}
