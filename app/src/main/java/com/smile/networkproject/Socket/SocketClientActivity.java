package com.smile.networkproject.Socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smile.networkproject.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author: smile .
 * date: On 2018/9/19
 */
public class SocketClientActivity extends AppCompatActivity {

    public static final int CODE_SOCKET_CONNECT = 8688;
    private Button bt_send;
    private EditText et_receive;
    private PrintWriter mPrintWriter;
    private TextView tv_message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);
        initView();
        // 开启服务端
        Intent intent = new Intent(this, SocketServerService.class);
        startService(intent);
        new Thread() {
            @Override
            public void run() {
                // 连接到服务端
                connectSocketServer();
            }
        }.start();
    }

    private void initView() {
        et_receive = (EditText) findViewById(R.id.et_receive);
        bt_send = (Button) findViewById(R.id.bt_send);
        tv_message = (TextView) this.findViewById(R.id.tv_message);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = et_receive.getText().toString();
                //向服务器发送信息
                if (!TextUtils.isEmpty(msg) && null != mPrintWriter) {
                    mPrintWriter.println(msg);
                    tv_message.setText(tv_message.getText() + "\n" + getCurrentTime() + "\n" + "客户端：" + msg);
                    et_receive.setText("");
                }
            }
        });
    }

    private void connectSocketServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                // 一个流套接字，连接到服务端
                socket = new Socket("localhost", CODE_SOCKET_CONNECT);
                // 获取到客户端的文本输出流
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            } catch (IOException e) {
                e.printStackTrace();
                SystemClock.sleep(1000);
            }
        }

        try {
            // 接收服务端发送的消息
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!isFinishing()) {
                final String str = reader.readLine();
                if (str != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_message.setText(tv_message.getText() + "\n" + getCurrentTime() + "\n" + "服务端：" + str);
                        }
                    });
                }
            }
            mPrintWriter.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

}
