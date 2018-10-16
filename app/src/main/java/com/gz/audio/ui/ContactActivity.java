package com.gz.audio.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gz.audio.R;
import com.gz.audio.http.HttpConst;
import com.gz.audio.ui.main.MainActivity1;
import com.gz.audio.utils.CountDownUtil;
import com.gz.audio.utils.IntentUtil;
import com.gz.audio.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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
