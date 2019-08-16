package com.develop.wiseisland.android.module.test1;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.base.BaseActivity;

public class Main1Activity extends BaseActivity {
    TextView tv_text;
    Button btn_start;
    Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initClick();
    }

    public void initView() {
        tv_text = findView(R.id.tv_text);
        btn_start = findView(R.id.btn_start);
    }

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            dialog = new Dialog(Main1Activity.this);
            dialog.setTitle("加载中...");
            dialog.setCancelable(true);
            dialog.show();
        }
    };

    public void initData() {

    }

    public void initClick() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("test");
                sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

}
