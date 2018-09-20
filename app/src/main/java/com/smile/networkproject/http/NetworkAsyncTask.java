package com.smile.networkproject.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * author: smile .
 * date: On 2018/8/21
 */
public class NetworkAsyncTask extends AsyncTask<String, Integer, Map<String, String>> {

    //NETWORK_GET表示发送GET请求
    public static final String NETWORK_GET = "NETWORK_GET";
    //NETWORK_POST_KEY_VALUE表示用POST发送键值对数据
    public static final String NETWORK_POST_KEY_VALUE = "NETWORK_POST_KEY_VALUE";
    //NETWORK_POST_XML表示用POST发送XML数据
    public static final String NETWORK_POST_XML = "NETWORK_POST_XML";
    //NETWORK_POST_JSON表示用POST发送JSON数据
    public static final String NETWORK_POST_JSON = "NETWORK_POST_JSON";

    @Override
    protected Map<String, String> doInBackground(String... params) {
        Map<String, String> result = new HashMap<String, String>();
        URL url = null;  //请求的URL地址
        HttpURLConnection connection = null;
        String requestHeader = null;  // 请求头
        String requestBody = null;    // 请求体
        String responseHeader = null;   // 响应头
        String responseBody = null;   // 请求体
        String action = params[0];
        try {
            switch (action) {
                case NETWORK_GET:
                    url = new URL("http://192.168.31.200:8080/HttpServer/MyServlet?name=孙群&age=27");
                    connection = (HttpURLConnection) url.openConnection();
                    // HttpURLConnection默认就是用GET发送请求，所以下面的setRequestMethod可以省略
                    connection.setRequestMethod("GET");
                    // HttpURLConnection默认也支持从服务端读取结果流，所以下面的setDoInput也可以省略
                    connection.setDoInput(true);
                    // 用setRequestProperty方法设置一个自定义的请求头:action，由于后端判断
                    connection.setRequestProperty("action", action);
                    // 禁用网络缓存
                    connection.setUseCaches(false);
                    // 获取请求头
                    requestHeader = getReqeuseteHeader(connection);
                    // 建立连接
                    connection.connect();
                    // 接收服务器返回的数据
                    InputStream is = connection.getInputStream();
                    responseHeader = getResponseHeader(connection);
                    requestBody = converInputSteamtoString(is);
                    break;
                case NETWORK_POST_KEY_VALUE:
                    break;
                case NETWORK_POST_XML:
                    break;
                case NETWORK_POST_JSON:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        result.put("url", url.toString());
        result.put("action", action);
        result.put("requestHeader", requestHeader);
        result.put("requestBody", requestBody);
        result.put("responseHeader", responseHeader);
        result.put("responseBody", responseBody);
        return result;
    }


    //  获取请求头
    private String getReqeuseteHeader(HttpURLConnection connection) {
        Map<String, List<String>> requestHeaderMap = connection.getRequestProperties();
        Iterator<String> requestHeaderIteator = requestHeaderMap.keySet().iterator();
        StringBuilder sbRequestHeader = new StringBuilder();
        while (requestHeaderIteator.hasNext()) {
            String requestHeaderKey = requestHeaderIteator.next();
            String requestHeaderValue = connection.getRequestProperty(requestHeaderKey);
            sbRequestHeader.append(requestHeaderKey);
            sbRequestHeader.append(":");
            sbRequestHeader.append(requestHeaderValue);
            sbRequestHeader.append("\n");
        }
        Log.e("TAG", "getReqeuseteHeader------: " + sbRequestHeader.toString());
        return sbRequestHeader.toString();
    }

    //  获取响应头
    private String getResponseHeader(HttpURLConnection connection) {
        Map<String, List<String>> responseHeaderMap = connection.getHeaderFields();
        Iterator<String> responseHeaderIteator = responseHeaderMap.keySet().iterator();
        StringBuilder sbResponseHeader = new StringBuilder();
        while (responseHeaderIteator.hasNext()) {
            String responseHeaderKey = responseHeaderIteator.next();
            String responseHeaderValue = connection.getRequestProperty(responseHeaderKey);
            sbResponseHeader.append(responseHeaderKey);
            sbResponseHeader.append(":");
            sbResponseHeader.append(responseHeaderValue);
            sbResponseHeader.append("\n");
        }
        Log.e("TAG", "getResponseHeader------: " + sbResponseHeader.toString());
        return sbResponseHeader.toString();
    }

    private String converInputSteamtoString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(Map<String, String> result) {
        super.onPostExecute(result);
        String url = result.get("url");
        String action = result.get("action");//http请求的操作类型
        String requestHeader = result.get("requestHeader");//请求头
        String requestBody = result.get("requestBody");//请求体
        String responseHeader = result.get("responseHeader");//响应头
        String responseBody = result.get("responseBody");//响应体

    }
}
