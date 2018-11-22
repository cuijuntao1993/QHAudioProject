package com.gz.audio.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;


import com.gz.audio.R;



/**
 * Created by Liuyz on 2018/5/22.
 */

public class ContactActivity extends BaseActivity {
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView( R.layout.activity_contact);


        initView();
        initListener();
    }


    private void initView() {

    }

    private void initListener() {
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
