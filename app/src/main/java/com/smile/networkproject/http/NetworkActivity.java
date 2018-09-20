package com.smile.networkproject.http;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.smile.networkproject.Person;
import com.smile.networkproject.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * author: smile .
 * date: On 2018/8/21
 */
public class NetworkActivity extends AppCompatActivity {
    private NetworkAsyncTask networkAsyncTask;
    private TextView tvUrl;
    private TextView tvRequestHeader;
    private TextView tvRequestBody;
    private TextView tvResponseHeader;
    private TextView tvResponseBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        tvUrl = (TextView) findViewById(R.id.tvUrl);
        tvRequestHeader = (TextView) findViewById(R.id.tvRequestHeader);
        tvRequestBody = (TextView) findViewById(R.id.tvRequestBody);
        tvResponseHeader = (TextView) findViewById(R.id.tvResponseHeader);
        tvResponseBody = (TextView) findViewById(R.id.tvResponseBody);
        networkAsyncTask = new NetworkAsyncTask();
        String action = getIntent().getStringExtra("action");
        Log.e("NetworkActivity", "onCreate: ------"+action);
        networkAsyncTask.execute(action);
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, Map<String, String>> {

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
                    // 发送get请求
                    case NETWORK_GET:
                        url = new URL("http://www.xianleshen:8080/springmvc/MyServlet/data?name=谦行&age=23");
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
                        // 获取响应头
                        responseHeader = getResponseHeader(connection);
                        // 获取响应体
                        responseBody = converInputSteamtoString(is);
                        break;
                    //  post 发送键值对数据
                    case NETWORK_POST_KEY_VALUE:
                        url = new URL("http://www.xianleshen:8080/springmvc/MyServlet/data");
                        // 建立连接 设置post 请求
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        // URL 连接可用于输入。如果打算使用 URL 连接进行输入，则将 DoInput 标志设置为 true；如果不打算使用，则设置为 false。默认值为 true
                        connection.setDoInput(true);
                        // URL 连接可用于输出。如果打算使用 URL 连接进行输出，则将 DoOutput 标志设置为 true；如果不打算使用，则设置为 false。默认值为 false。
                        connection.setDoOutput(true);
                        // 用setRequestProperty方法设置一个自定义的请求头:action
                        connection.setRequestProperty("action", NETWORK_POST_KEY_VALUE);
                        // 获取请求头
                        requestHeader = getReqeuseteHeader(connection);
                        // 获取conn的输出流
                        OutputStream os = connection.getOutputStream();
                        requestBody = new String("name=谦行&age=23");
                        // 将请求体写入到conn的输出流中
                        os.write(requestBody.getBytes());
                        // 将这些字节立即写入它们预期的目标
                        os.flush();
                        // 将输出流关闭
                        os.close();
                        // 接收服务器返回的数据
                        is = connection.getInputStream();
                        // 获取响应头
                        responseHeader = getResponseHeader(connection);
                        // 获取响应体
                        responseBody = converInputSteamtoString(is);
                        break;
                    // 用POST发送xml 数据
                    case NETWORK_POST_XML:
                        url = new URL("http://www.xianleshen:8080/springmvc/MyServlet/data");
                        // 建立连接 设置post 请求
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        // 用setRequestProperty方法设置一个自定义的请求头:action
                        connection.setRequestProperty("action", NETWORK_POST_XML);
                        // URL 连接可用于输出。如果打算使用 URL 连接进行输出，则将 DoOutput 标志设置为 true；如果不打算使用，则设置为 false。默认值为 false。
                        connection.setDoOutput(true);
                        // 获取请求头
                        requestHeader = getReqeuseteHeader(connection);
                        // 获取conn 的输入流
                        os = connection.getOutputStream();
                        // 读取当前xml的内容
                        requestBody = getStringFromAssets("request.xml");
                        // 将请求体写入到conn的输出流中
                        os.write(requestBody.getBytes());
                        // 将这些字节立即写入它们预期的目标
                        os.flush();
                        // 将输出流关闭
                        os.close();
                        // 接收服务器返回的数据
                        is = connection.getInputStream();
                        // 获取响应头
                        responseHeader = getResponseHeader(connection);
                        // 获取响应体
                        responseBody = converXmltoString(is);
                        break;
                    // 用POST发送json数据
                    case NETWORK_POST_JSON:
                        url = new URL("http://www.xianleshen:8080/springmvc/MyServlet/data");
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setRequestProperty("action", NETWORK_POST_JSON);
                        requestHeader = getReqeuseteHeader(connection);
                        os = connection.getOutputStream();
                        requestBody = getStringFromAssets("request.json");
                        os.write(requestBody.getBytes());
                        os.flush();
                        os.close();
                        is = connection.getInputStream();
                        responseHeader = getResponseHeader(connection);
                        responseBody = converJsontoString(is);
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

        @Override
        protected void onPostExecute(Map<String, String> result) {
            super.onPostExecute(result);
            String url = result.get("url");
//            String action = result.get("action");//http请求的操作类型
            String requestHeader = result.get("requestHeader");//请求头
            String requestBody = result.get("requestBody");//请求体
            String responseHeader = result.get("responseHeader");//响应头
            String responseBody = result.get("responseBody");//响应体
            tvUrl.setText(url);
            tvRequestHeader.setText(requestHeader);
            tvRequestBody.setText(requestBody);
            tvResponseHeader.setText(responseHeader);
            tvResponseBody.setText(responseBody);
        }

        // 获取assets 文件夹下的文件内容
        private String getStringFromAssets(String fileName) {
            String body = null;
            try {
                InputStream is = getAssets().open(fileName);
                int lenght = is.available();
                byte[] bytes = new byte[lenght];
                is.read(bytes);
                body = new String(bytes, "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return body;
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

        // 转换InputSteamt 为 String 类型
        private String converInputSteamtoString(InputStream is) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        }

        // 转换xml 为 String 类型
        private String converXmltoString(InputStream is) {
            StringBuffer sb = new StringBuffer();
            List<Person> list = null;
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is));
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String name = parser.getName();
                    Person person = null;
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            list = new ArrayList<Person>();
                            break;
                        case XmlPullParser.START_TAG:
                            if ("Person".equals(name)) {
                                person = new Person();
                            }
                            if ("name".equals(name)) {
                                if (person != null) {
                                    person.setName(parser.nextText());
                                }
                            }
                            if ("age".equals(name)) {
                                if (person != null) {
                                    person.setAge(Integer.parseInt(parser.nextText()));
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if ("Person".equals(name)) {
                                list.add(person);
                            }
                            break;
                    }
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (list != null) {
                for (Person person : list) {
                    sb.append(person.toString() + "\n");
                }
            }
            return sb.toString();
        }

        // 转换Json 为 String 类型
        private String converJsontoString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            List<Person> list = null;
            StringBuffer sb = new StringBuffer();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject object = new JSONObject(sb.toString());
                if (object.has("Persons") && object.get("Persons") != null) {
                    JSONArray array = new JSONArray(object.get("Persons").toString());
                    for (int i = 0; i < array.length(); i++) {
                        Person person = new Person();
                        JSONObject object1 = array.getJSONObject(i);
                        if (object1.has("name") && object1.get("name") != null) {
                            person.setName(object1.get("name").toString());
                        }
                        if (object1.has("age") && object1.get("age") != null) {
                            person.setAge(Integer.parseInt(object1.get("name").toString()));
                        }
                        list.add(person);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list != null) {
                for (Person person : list) {
                    sb.append(person.toString() + "\n");
                }
            }
            return sb.toString();
        }
    }

}
