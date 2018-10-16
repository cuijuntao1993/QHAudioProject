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
import com.gz.audio.utils.SharePreferenceUtil;
import com.gz.audio.utils.ToastUtil;

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

public class RegistActivity extends BaseActivity {
    private ProgressDialog dialog;

    View back;
    EditText et_mobile,et_pwd,et_code;
    Button btn_send_code,regist_btn;
    static Integer TOAST_MSG = 101;
    static Integer CODE_MSG = 102;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TOAST_MSG){
                Toast.makeText(RegistActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
            }
            if (msg.what == CODE_MSG){
                new CountDownUtil(btn_send_code)
                        .setCountDownMillis(60_000L)//倒计时60000ms
                        .setCountDownColor(R.color.green_code,R.color.gray_code)//不同状态字体颜色
                        .start();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView( R.layout.regist_activity);


        initView();
        initListener();
    }


    private void initView() {
        btn_send_code = findViewById(R.id.btn_send_code);
        et_mobile = findViewById(R.id.et_mobile);
        regist_btn = findViewById(R.id.regist_btn);
        et_pwd = findViewById(R.id.et_pwd);
        et_code = findViewById(R.id.et_code);
        back = findViewById(R.id.back);
    }

    private void initListener() {
        btn_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMsmCode(et_mobile.getText().toString().trim());
            }
        });
        regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regist(et_mobile.getText().toString().trim(),et_pwd.getText().toString().trim(),et_code.getText().toString());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
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

    private void getMsmCode(String mobile){

        if (mobile.length()!=11){
            Toast.makeText(this,"手机号码格式错误!",Toast.LENGTH_SHORT).show();
            return;
        }


        dialog = ProgressDialog.show(this, "", "获取验证码中...", false, true);

        Map map = new HashMap();
        map.put("telephone",mobile);
        map.put("smsType","register");
        JSONObject object = new JSONObject(map);

        RequestBody requestBody = RequestBody.create(HttpConst.JSON_MEDIA_TYPE, object.toString());

        Request request = new Request.Builder()
                .url(HttpConst.BASE_URL + "ecg/request_SMS_code.do")
                .post(requestBody)
                .build();

        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.showLog("..onFailure....." + e.toString());
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                LogUtil.showLog("..onResponse.......json...." + json);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject object = new JSONObject(json);
                        if (0 == object.getInt("status")) {
                            if (0 == object.getInt("status")) {
                                Message msg = new Message();
                                msg.what = CODE_MSG;
                                msg.obj = object.getString("msg");
                                handler.sendMessage(msg);
                            }
                        }
                        Message msg = new Message();
                        msg.what = TOAST_MSG;
                        msg.obj = object.getString("msg");
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }
        });
    }



    private void regist(final String telephone,final String password,String smsCode){

        if (telephone.length()!=11){
            Toast.makeText(this,"手机号码格式错误!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length()<6 || password.length()>16){
            Toast.makeText(this,"密码长度错误!",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = ProgressDialog.show(this, "", "注册用户中...", false, true);

        Map map = new HashMap();
        map.put("telephone",telephone);
        map.put("smsCode",smsCode);
        map.put("password",password);

        JSONObject object = new JSONObject(map);

        RequestBody requestBody = RequestBody.create(HttpConst.JSON_MEDIA_TYPE, object.toString());

        Request request = new Request.Builder()
                .url(HttpConst.BASE_URL + "ecg/sign_up.do")
                .post(requestBody)
                .build();

        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.showLog("..onFailure....." + e.toString());
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                LogUtil.showLog("..onResponse.......json...." + json);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject object = new JSONObject(json);
                        if (0 == object.getInt("status")) {
                            login(telephone,password);
                        }
                            Message msg = new Message();
                        msg.what = TOAST_MSG;
                        msg.obj = object.getString("msg");
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }
        });
    }

    private void login(String telephone,String password){
        if (telephone.length()!=11){
            Toast.makeText(this,"手机号码格式错误!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length()<6 || password.length()>16){
            Toast.makeText(this,"密码长度错误!",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = ProgressDialog.show(this, "", "登录中...", false, true);

        Map map = new HashMap();
        map.put("telephone",telephone);
        map.put("password",password);

        JSONObject object = new JSONObject(map);

        RequestBody requestBody = RequestBody.create(HttpConst.JSON_MEDIA_TYPE, object.toString());

        Request request = new Request.Builder()
                .url(HttpConst.BASE_URL + "ecg/login_pwd.do")
                .post(requestBody)
                .build();

        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.showLog("..onFailure....." + e.toString());
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                LogUtil.showLog("..onResponse.......json...." + json);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject object = new JSONObject(json);

                        if (0 == object.getInt("status")) {
                            JSONObject dataObj = object.getJSONObject("data");
                            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("id", dataObj.getInt("id"));
                            editor.putString("telephone", dataObj.getString("telephone"));
                            editor.putString("password", dataObj.getString("password"));
                            editor.putString("smsCode", dataObj.getString("smsCode"));
                            editor.putString("token", dataObj.getString("token"));

                            editor.commit();

                            IntentUtil.startActivity(RegistActivity.this,MainActivity1.class);
                        }

                        Message msg = new Message();
                        msg.what = TOAST_MSG;
                        msg.obj = object.getString("msg");
                        handler.sendMessage(msg);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
            }
        });
    }
}
