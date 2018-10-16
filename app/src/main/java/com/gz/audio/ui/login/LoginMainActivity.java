package com.gz.audio.ui.login;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.gz.audio.R;
import com.gz.audio.ui.BaseActivity;

/**
 * Created by acer on 2018/7/30.
 */

public class LoginMainActivity extends BaseActivity implements View.OnClickListener{
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private RadioButton rb_shoprank, rb_share;
    View back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main_activity);

        initView();

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.content_layout, new PwdLoginFragment());
        transaction.commit();

    }

    // 初始化视图
    public void initView() {
        rb_shoprank = (RadioButton) findViewById(R.id.pwd_login);
        rb_share = (RadioButton) findViewById(R.id.code_login);


        rb_shoprank.setOnClickListener(this);
        rb_share.setOnClickListener(this);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // 点击方法
    @Override
    public void onClick(View v) {
        // 点击时启动trancaction事件
        transaction = manager.beginTransaction();

        switch (v.getId()) {
            case R.id.pwd_login:
                transaction.replace(R.id.content_layout, new PwdLoginFragment());
                break;
            case R.id.code_login:
                transaction.replace(R.id.content_layout, new CodeLoginFragment());
                break;

            default:
                break;
        }

        transaction.commit();
    }
}
