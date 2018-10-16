package com.gz.audio.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.gz.audio.R;
import com.gz.audio.ui.login.LoginMainActivity;
import com.gz.audio.utils.IntentUtil;

/**
 * Created by acer on 2018/7/30.
 */

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_login);

        initView();
//        initListener();
    }
    private void initView(){
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(LoginActivity.this, LoginMainActivity.class);
            }
        });
        findViewById(R.id.regist_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(LoginActivity.this, RegistActivity.class);

            }
        });
    }
}
