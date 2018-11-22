package com.gz.audio.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gz.audio.R;
import com.gz.audio.utils.IntentUtil;


/**
 * Created by Liuyz on 2018/5/22.
 */

public class LogoutActivity extends BaseActivity {
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView( R.layout.activity_logout);


        initView();
        initListener();
    }


    private void initView() {

    }

    private void initListener() {
        findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.clear_cache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LogoutActivity.this,"已清理缓存",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
