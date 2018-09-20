package com.smile.networkproject.Volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smile.networkproject.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * author: smile .
 * date: On 2018/9/2
 */
public class ShowDataActivity extends AppCompatActivity {

    private String type;
    private TextView tvData;
    private ImageView ivData;
    private NetworkImageView nivData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        tvData = (TextView) findViewById(R.id.tvData);
        ivData = (ImageView) findViewById(R.id.ivData);
        nivData = (NetworkImageView) findViewById(R.id.nivData);

        type = getIntent().getStringExtra("type");
        RequestQueue mQueue = Volley.newRequestQueue(this);
        switch (type) {
            case "String":
                ivData.setVisibility(View.GONE);
                nivData.setVisibility(View.GONE);
                StringRequest mStringRequest = new StringRequest(Request.Method.GET, "https://www.apiopen.top/meituApi",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                tvData.setText(s);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(type, "onErrorResponse: " + volleyError);
                    }
                });
                mQueue.add(mStringRequest);
                break;
            case "JsonObject":
                ivData.setVisibility(View.GONE);
                nivData.setVisibility(View.GONE);
                JSONObject object = new JSONObject();
                try {
                    object.put("page", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "https://www.apiopen.top/meituApi", object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                tvData.setText(response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(type, "onErrorResponse: " + error);
                    }
                });
                mQueue.add(mJsonObjectRequest);
                break;
            case "Image":
                tvData.setVisibility(View.GONE);
                nivData.setVisibility(View.GONE);
                ImageRequest mImageRequest = new ImageRequest("http://7xi8d6.com1.z0.glb.clouddn.com/2017-10-31-nozomisasaki_official_31_10_2017_10_49_17_24.jpg",
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                ivData.setImageBitmap(response);
                            }
                        }, 400, 400, Bitmap.Config.ARGB_8888,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(type, "onErrorResponse: " + error);
                            }
                        });
                mQueue.add(mImageRequest);
                break;
            case "Imageloader":
                tvData.setVisibility(View.GONE);
                nivData.setVisibility(View.GONE);
                ImageLoader mImageLoader = new ImageLoader(mQueue, new ImageCache());
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(ivData, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
                mImageLoader.get("http://7xi8d6.com1.z0.glb.clouddn.com/20171107100244_0fbENB_yyannwong_7_11_2017_10_2_5_982.jpeg", imageListener);
                break;
            case "NetworkImage":
                tvData.setVisibility(View.GONE);
                ivData.setVisibility(View.GONE);
                mImageLoader = new ImageLoader(mQueue, new ImageCache());
                nivData.setDefaultImageResId(R.mipmap.ic_launcher);
                nivData.setErrorImageResId(R.mipmap.ic_launcher);
                nivData.setImageUrl("http://7xi8d6.com1.z0.glb.clouddn.com/20171025112955_lmesMu_katyteiko_25_10_2017_11_29_43_270.jpeg", mImageLoader);
                break;
        }
    }
}
