package com.smile.networkproject.OkHttp;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.smile.networkproject.R;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author: smile .
 * date: On 2018/9/2
 */
public class OKHttpTestActivity extends AppCompatActivity {

    private TextView tvData;
    private ImageView ivData;
    private NetworkImageView nivData;

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 111:
                    Bundle bundle = msg.getData();
                    tvData.setText(bundle.getString("body"));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        tvData = (TextView) findViewById(R.id.tvData);
        ivData = (ImageView) findViewById(R.id.ivData);
        nivData = (NetworkImageView) findViewById(R.id.nivData);

        ivData.setVisibility(View.GONE);
        nivData.setVisibility(View.GONE);
    }

    /**
     * 异步get请求
     */
    private void sendGet() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://blog.csdn.net/javasxl").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what = 111;
                Bundle bundle = new Bundle();
                bundle.putString("body", response.body().string());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
    }

    private void sendOKHttpTest(){
        OKHttpTest.mInstance.getAsynHttp("https://blog.csdn.net/javasxl", new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }
        });
    }

    /**
     * 异步Post请求
     */
    private void sendPost() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("page", "1").build();
        Request request = new Request.Builder().url("https://www.apiopen.top/meituApi").
                post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = new Message();
                msg.what = 111;
                Bundle bundle = new Bundle();
                bundle.putString("body", response.body().string());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
    }


    /**
     * 异步上传单文件上传
     */
    private void sendFile() {
        OkHttpClient okHttpClient = new OkHttpClient();
        String filePath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        File file = new File(filePath, "Test.txt");
        Request request = new Request.Builder().url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file)).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("OKHttp", "onResponse----: " + response.body().string());
            }
        });
    }


    /**
     * 异步上传单文件且携带其他form参数上传
     */
    private void sendMultipart() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("title", "Image")
                .addFormDataPart("image", "test.png", RequestBody.create(MEDIA_TYPE_PNG,
                        new File("sdcard/test.png"))).build();
        Request request = new Request.Builder().url("https://api.github.com/image")
                .post(requestBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("OKHttp", "onResponse----: " + response.body().string());
            }
        });

    }
}
