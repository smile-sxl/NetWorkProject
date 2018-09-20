package com.smile.networkproject.http;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.smile.networkproject.R;

public class HttpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGet;
    private Button btnPostKey;
    private Button btnPostXml;
    private Button btnPostJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        btnGet = (Button) findViewById(R.id.btnGet);
        btnPostKey = (Button) findViewById(R.id.btnPostKey);
        btnPostXml = (Button) findViewById(R.id.btnPostXml);
        btnPostJson = (Button) findViewById(R.id.btnPostJson);

        btnGet.setOnClickListener(this);
        btnPostKey.setOnClickListener(this);
        btnPostXml.setOnClickListener(this);
        btnPostJson.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGet:
                Intent intent = new Intent(this, NetworkActivity.class);
                intent.putExtra("action", "NETWORK_GET");
                startActivity(intent);
                break;
            case R.id.btnPostKey:
                intent = new Intent(this, NetworkActivity.class);
                intent.putExtra("action", "NETWORK_POST_KEY_VALUE");
                startActivity(intent);
                break;
            case R.id.btnPostXml:
                intent = new Intent(this, NetworkActivity.class);
                intent.putExtra("action", "NETWORK_POST_XML");
                startActivity(intent);
                break;
            case R.id.btnPostJson:
                intent = new Intent(this, NetworkActivity.class);
                intent.putExtra("action", "NETWORK_POST_JSON");
                startActivity(intent);
                break;
        }
    }
}
