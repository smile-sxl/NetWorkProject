package com.smile.networkproject.Volley;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.smile.networkproject.R;

public class VolleyActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnString;
    private Button btnJsonObject;
    private Button btnImage;
    private Button btnImageLoader;
    private Button btnNetworkImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        btnString = (Button) findViewById(R.id.btnString);
        btnJsonObject = (Button) findViewById(R.id.btnJsonObject);
        btnImage = (Button) findViewById(R.id.btnImage);
        btnImageLoader = (Button) findViewById(R.id.btnImageLoader);
        btnNetworkImage = (Button) findViewById(R.id.btnNetworkImage);

        btnString.setOnClickListener(this);
        btnJsonObject.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnImageLoader.setOnClickListener(this);
        btnNetworkImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnString:
                Intent intent = new Intent(this, ShowDataActivity.class);
                intent.putExtra("type", "String");
                startActivity(intent);
                break;
            case R.id.btnJsonObject:
                intent = new Intent(this, ShowDataActivity.class);
                intent.putExtra("type", "JsonObject");
                startActivity(intent);
                break;
            case R.id.btnImage:
                intent = new Intent(this, ShowDataActivity.class);
                intent.putExtra("type", "Image");
                startActivity(intent);
                break;
            case R.id.btnImageLoader:
                intent = new Intent(this, ShowDataActivity.class);
                intent.putExtra("type", "Imageloader");
                startActivity(intent);
                break;
            case R.id.btnNetworkImage:
                intent = new Intent(this, ShowDataActivity.class);
                intent.putExtra("type", "NetworkImage");
                startActivity(intent);
                break;
        }
    }
}
