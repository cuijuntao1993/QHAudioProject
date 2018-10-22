package com.gz.audio.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gz.audio.R;
import com.gz.audio.http.HttpConst;
import com.gz.audio.ui.BaseFragment;
import com.gz.audio.ui.MainActivity;
import com.gz.audio.ui.RegistActivity;
import com.gz.audio.ui.ResetPwdActivity;
import com.gz.audio.ui.main.MainActivity1;
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
 * Created by acer on 2018/7/30.
 */

public class PwdLoginFragment extends BaseFragment{
//    private TextView textView;

    View view = null;
    private static final String TAG = PwdLoginFragment.class.getSimpleName();//得到“CommonFrameFragment”

    EditText et_pwd,et_mobile;
    Button btn_login;
    private ProgressDialog dialog;

    static Integer TOAST_MSG = 101;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TOAST_MSG){

                if(msg.obj.toString().equals("登录成功")){
                    IntentUtil.startActivity(getActivity(),MainActivity1.class);
                    Toast.makeText(getActivity(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG,"框架Fragment页面被初始化了...");
        view = inflater.inflate(R.layout.pwd_login_fragment,null);
        initView();
        return view;
    }

    private void initView(){
        view.findViewById(R.id.tv_reset_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getActivity(), ResetPwdActivity.class);
            }
        });

        et_pwd = view.findViewById(R.id.et_pwd);
        et_mobile = view.findViewById(R.id.et_mobile);
        btn_login = view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(et_mobile.getText().toString().trim(),et_pwd.getText().toString().trim());
            }
        });
    }
    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG, "框架Fragment数据被初始化了...");
//        textView.setText("密码登录");
    }


    private void login(String telephone,String password){
        if (telephone.length()!=11){
            Toast.makeText(getActivity(),"手机号码格式错误!",Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length()<6 || password.length()>16){
            Toast.makeText(getActivity(),"密码长度错误!",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog = ProgressDialog.show(getActivity(), "", "登录中...", false, true);

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
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("id", dataObj.getInt("id"));
                            editor.putString("telephone", dataObj.getString("telephone"));
                            editor.putString("password", dataObj.getString("password"));
                            editor.putString("smsCode", dataObj.getString("smsCode"));
                            editor.putString("token", dataObj.getString("token"));

                            editor.commit();
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
