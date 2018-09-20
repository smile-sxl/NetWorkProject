package com.smile.networkproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smile.networkproject.OkHttp.OKHttpTestActivity;
import com.smile.networkproject.Retrofit.RetrofitTestActivity;
import com.smile.networkproject.Socket.SocketClientActivity;
import com.smile.networkproject.Volley.VolleyActivity;
import com.smile.networkproject.http.HttpActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvList;
    private MyAdapter myAdapter;
    private List<MyClass> dataLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataLists = new ArrayList<MyClass>();

        dataLists.add(new MyClass("Socket", SocketClientActivity.class));
        dataLists.add(new MyClass("HttpClientConnection 和 HttpURLConnection", HttpActivity.class));
        dataLists.add(new MyClass("Velley", VolleyActivity.class));
        dataLists.add(new MyClass("OkHttp", OKHttpTestActivity.class));
        dataLists.add(new MyClass("Retrofit", RetrofitTestActivity.class));

        rvList = (RecyclerView) findViewById(R.id.rvList);
        myAdapter = new MyAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setAdapter(myAdapter);
        rvList.addItemDecoration(new DividerItemDecoration(this, 1));
        myAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void myClick(int position) {
                Intent intent = new Intent(MainActivity.this, dataLists.get(position).getMyClass());
                startActivity(intent);
            }
        });

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private Context context;
        private MyOnItemClickListener myOnClickListener;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_communication, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.allView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myOnClickListener.myClick(position);
                }
            });
            holder.tvTitle.setText(dataLists.get(position).getClassName());
        }

        @Override
        public int getItemCount() {
            return dataLists.size();
        }

        public void setOnItemClickListener(MyOnItemClickListener onClickListener) {
            this.myOnClickListener = onClickListener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public View allView;
            public TextView tvTitle;


            public ViewHolder(View itemView) {
                super(itemView);
                allView = itemView;
                tvTitle = itemView.findViewById(R.id.tvTitle);
            }
        }
    }

    private interface MyOnItemClickListener {
        public void myClick(int position);
    }
}
