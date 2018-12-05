package com.gz.audio.ui;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gz.audio.R;
import com.gz.audio.adapter.mHistoryAdapter;
import com.gz.audio.entiy.ECG_Records;
import com.gz.audio.entiy.json_array_item;
import com.gz.audio.http.HttpConst;
import com.gz.audio.interfaces.OnViewItemListener;
import com.gz.audio.ui.main.MainActivity1;
import com.gz.audio.utils.IntentUtil;
import com.gz.audio.utils.KDSharedPreferences;
import com.gz.audio.utils.LogUtil;
import com.gz.audio.utils.SharePreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;


/**
 * Created by Liuyz on 2018/5/22.
 */

public class HistoryActivity extends BaseActivity implements OnViewItemListener, AdapterView.OnItemClickListener {
//    private com.gz.audio.HistoryBinding binding;

    private mHistoryAdapter adapter;
    private List<ECG_Records> list = new ArrayList<>();
    private List<String> overList = null; // 记录已上传文件名称
    private MediaMetadataRetriever mmr;
    private String overVoiceMsg;
    private Handler mainHanlder;
    private File file;
    private int status = 1; // 0.成功  1.失败
    private String msg = ""; //记录返回数据
    private String tel;
    private ArrayList<json_array_item> json_array = new ArrayList<>();
    private ProgressDialog dialog;
    //测试删除数据
    private TextView history_delete;

    ListView history_listview;
    View back;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog = ProgressDialog.show(HistoryActivity.this, "", "诊断结果更新中...", false, false);
            if (msg.what == 1){
                try {
                    JSONObject object = (JSONObject) msg.obj;
                    json_array_item jsonArrayItem = new json_array_item();
                    if(object.getString("msg").toString().equals("记录数据成功")){
                        JSONObject object_array =object.getJSONObject("data");
                        JSONArray jsonArray = object_array.getJSONArray("data");
                        for (int i =0;i<jsonArray.length();i++){
                            JSONObject item = (JSONObject) jsonArray.get(i);
                            if(item.getInt("state") == 2){
                                jsonArrayItem.setState(item.getInt("state"));
                                jsonArrayItem.setRecord_id(item.getInt("recordId"));
                                jsonArrayItem.setDiagnose_abstract(item.getString("diagnoseAbstract"));
                                jsonArrayItem.setDiagnose_details(item.getString("diagnoseDetails"));
//                                json_array.add(jsonArrayItem);
                                //更新本地条目
                                ContentValues values = new ContentValues();
                                values.put("State","2");
                                values.put("Diagnose_abstract",item.getString("diagnoseAbstract"));
                                values.put("Diagnose_details",item.getString("diagnoseDetails"));
                                LitePal.updateAll(ECG_Records.class,values,"Record_ID = ?",item.getInt("recordId")+"");
                            }
                        }
                        //记录更新完成
                        dialog.dismiss();

                        list.clear();
                        list.addAll(LitePal.findAll(ECG_Records.class));
                        adapter.notifyDataSetChanged();

                    }else{
                        Toast.makeText(HistoryActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // 更新UI线程
        mainHanlder = new Handler(Looper.getMainLooper());
        KDSharedPreferences sharedPreferences = KDSharedPreferences.getInstence();
        tel = sharedPreferences.getString("telephone", "18612789735");
        initView();
        initData();
    }

    private void initView() {
        history_listview = findViewById(R.id.history_listview);
        history_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ECG_Records click_bean = list.get(position);
                Intent intent = new Intent(HistoryActivity.this,XD_InFormation.class);
                intent.putExtra("user",click_bean);
                startActivity(intent);
            }
        });
        back = findViewById(R.id.back);

        overVoiceMsg = SharePreferenceUtil.getString("over_voice_list");
        if (!TextUtils.isEmpty(overVoiceMsg)) {
            overList = gson.fromJson(overVoiceMsg, List.class);
        } else {
            overList = new ArrayList<>();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //测试删除数据
        history_delete= findViewById(R.id.history_delete);
        history_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list = LitePal.findAll(ECG_Records.class);
                adapter = new mHistoryAdapter(HistoryActivity.this,list);
                history_listview.setAdapter(adapter);
                overList = new ArrayList<>();
                if(null!= list){
                    for (int i = 0;i<list.size();i++){
                        list.get(i).delete();
                    }
                }

            }
        });
    }

    @Override
    public void onItemClick(final int position) {

    }

    private void initData() {
        list = LitePal.findAll(ECG_Records.class);
        adapter = new mHistoryAdapter(this,list);
        history_listview.setAdapter(adapter);
        overList = new ArrayList<>();
        if(null!= list){
            for (int i = 0;i<list.size();i++){
                if(list.get(i).getState() == 1){
                    overList.add(list.get(i).getRecord_ID()+"");
                }
            }
        }
        uploadByCheck();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    //循环发送，请求诊断结果
    private void uploadByCheck(){
        KDSharedPreferences editor = KDSharedPreferences.getInstence();
        final String authtoken = editor.getString("token","");
        int user_id = editor.getInt("id",0);
        Map map = new HashMap();
        map.put("user_id",user_id);
        map.put("record_ids",overList);
        map.put("token",authtoken);
        JSONObject object = new JSONObject(map);
        RequestBody requestBody = RequestBody.create(HttpConst.JSON_MEDIA_TYPE, object.toString());

        Request request = new Request.Builder()
                .url("http://app.xinheyidian.com/xinheyidianecg/ecg/record/query_records.do")
                .post(requestBody)
                .build();

        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                //设置Auth
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        return response.request().newBuilder().header("Authorization", authtoken).build();
                    }
                })
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.showLog("..onFailure....." + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                LogUtil.showLog("..onResponse.......json...." + json);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject object = new JSONObject(json);

                        if (0 == object.getInt("status")) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = object;
                            handler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
}
