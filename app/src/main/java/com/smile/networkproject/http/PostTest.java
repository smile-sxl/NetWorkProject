package com.smile.networkproject.http;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * author: smile .
 * date: On 2018/8/21
 */
public class PostTest {

    private static HttpURLConnection getHttpURLConnection(String url) {
        HttpURLConnection mHttpURLConnection = null;
        try {
            URL mUrl = new URL(url);
            mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
            // 设置连接超时时间
            mHttpURLConnection.setConnectTimeout(15000);
            // 设置读取超时时间
            mHttpURLConnection.setReadTimeout(15000);
            // 设置请求参数
            mHttpURLConnection.setRequestMethod("POST");
            // 添加Header
            mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            // 接收输入流
            mHttpURLConnection.setDoInput(true);
            // 传递参数时开启
            mHttpURLConnection.setDoOutput(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mHttpURLConnection;
    }

    public static void postParams(OutputStream outputStream, List<NameValuePair> paramsList) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (NameValuePair pair : paramsList) {
            if (!TextUtils.isEmpty(sb)) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write(sb.toString());
        Log.e("TAG", "postParams:---- " + sb.toString() + "----" + outputStream.toString());
        writer.flush();
        writer.close();
    }

    private static void useHttpUrlConnectionPost(String url) {
        InputStream mInputStream = null;
        HttpURLConnection mHttpURLConnection = getHttpURLConnection(url);
        List<NameValuePair> postParams = new ArrayList<>();
        // 设置基本名称值对
        postParams.add(new BasicNameValuePair("ip", "192.168.0.111"));
        try {
            postParams(mHttpURLConnection.getOutputStream(), postParams);
            mHttpURLConnection.connect();
            mInputStream = mHttpURLConnection.getInputStream();
            int code = mHttpURLConnection.getResponseCode();
            String respose = converStremToString(mInputStream);
            Log.e("Tag", "请求状态码: " + code + "\n请求结果：\n" + respose);
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mHttpURLConnection.disconnect();
        }

    }

    // 转换InputStream 为String
    private static String converStremToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        String respose = sb.toString();
        return respose;
    }

    private void useHttpClientPost(String url) {
        HttpPost mHttpPost = new HttpPost(url);
        mHttpPost.addHeader("Connection", "keep-Alive");
        HttpClient mHttpClient = createHttpClient();
        List<NameValuePair> postParams = new ArrayList<>();
        // 要传递的参数
        postParams.add(new BasicNameValuePair("ip", "59.108.54.37"));
        try {
            // post 设置要传递的参数
            mHttpPost.setEntity(new UrlEncodedFormEntity(postParams));
            HttpResponse mHttpResponse = mHttpClient.execute(mHttpPost);
            // 得到返回的实体
            HttpEntity mHttpEntity = mHttpResponse.getEntity();
            // 得到返回的状态码
            int code = mHttpResponse.getStatusLine().getStatusCode();
            if (mHttpEntity != null) {
                InputStream mInputStream = mHttpEntity.getContent();
                String respose = converStremToString(mInputStream);
                Log.e("Tag", "请求状态码: " + code + "\n请求结果：\n" + respose);
                mInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpClient createHttpClient() {
        // 设置基本的参数
        HttpParams mDefaultHttpParams = new BasicHttpParams();

        //   设置HTTP连接参数  设置连接超时时间
        HttpConnectionParams.setConnectionTimeout(mDefaultHttpParams, 15000);
        // 设置请求超时时间
        HttpConnectionParams.setSoTimeout(mDefaultHttpParams, 15000);
        HttpConnectionParams.setTcpNoDelay(mDefaultHttpParams, true);
        // 设置Http 协议参数
        HttpProtocolParams.setVersion(mDefaultHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(mDefaultHttpParams, HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(mDefaultHttpParams, true);

        HttpClient mHttpClient = new DefaultHttpClient(mDefaultHttpParams);
        return mHttpClient;
    }


    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                useHttpUrlConnectionPost("http://ip.taobao.com/service/getIpInfo.php");
            }
        }).start();
    }

}
