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
import android.widget.Toast;

import com.gz.audio.R;
import com.gz.audio.adapter.mHistoryAdapter;
import com.gz.audio.entiy.ECG_Records;
import com.gz.audio.entiy.json_array_item;
import com.gz.audio.http.HttpConst;
import com.gz.audio.interfaces.OnViewItemListener;
import com.gz.audio.ui.main.MainActivity1;
import com.gz.audio.utils.IntentUtil;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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

    ListView history_listview;
    View back;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                try {
                    JSONObject object = (JSONObject) msg.obj;
                    json_array_item jsonArrayItem = new json_array_item();
                    if(object.getString("msg").toString().equals("查询成功")){
                        JSONArray jsonArray = object.getJSONArray("record_list");
                        for (int i =0;i<jsonArray.length();i++){
                            JSONObject item = (JSONObject) jsonArray.get(i);
                            if(item.getInt("state") == 2){
                                jsonArrayItem.setState(item.getInt("state"));
                                jsonArrayItem.setRecord_id(item.getInt("record_id"));
                                jsonArrayItem.setDiagnose_abstract(item.getString("diagnose_abstract"));
                                jsonArrayItem.setDiagnose_details(item.getString("diagnose_details"));
//                                json_array.add(jsonArrayItem);

                                //更新本地条目
                                ContentValues values = new ContentValues();
                                values.put("state","2");
                                values.put("diagnose_abstract",item.getString("diagnose_abstract"));
                                values.put("diagnose_details",item.getString("diagnose_details"));
                                LitePal.updateAll(ECG_Records.class,values,"record_id = ?",item.getInt("record_id")+"");
                            }
                        }

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
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
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
    }

    @Override
    public void onItemClick(final int position) {
//        file = new File(list.get(position).getFileaddress());
//        if (!file.exists()) {
//            return;
//        }
//        dialog = ProgressDialog.show(this, "", "音频上传中...", false, false);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("uploadAudioFile", file.getName(), fileBody)
//                .addFormDataPart("md5", FileUtils.getMD5Method(file))
//                .build();
//        Request request = new Request.Builder()
//                .url("http://59.110.23.176:8341/upload/")
//                .post(requestBody)
//                .build();
//
//        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
//        OkHttpClient okHttpClient = httpBuilder
//                //设置超时
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtil.showLog("..onFailure....." + e.toString());
//                mainHanlder.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.dismiss();
//                        ToastUtil.showToast(HistoryActivity.this, "上传失败!");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String json = response.body().string();
//                LogUtil.showLog("..onResponse.......json...." + json);
//                if (!TextUtils.isEmpty(json)) {
//                    try {
//                        JSONObject object = new JSONObject(json);
//                        if (object.has("status")) {
//                            status = object.getInt("status");
//                        }
//                        if (object.has("msg")) {
//                            msg = object.getString("msg");
//                        }
//                        mainHanlder.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                dialog.dismiss();
//                                ToastUtil.showToast(HistoryActivity.this, msg);
//                                if (status == 0) {
//                                    if (!overList.contains(file.getName())) {
//                                        overList.add(file.getName());
//                                        SharePreferenceUtil.putString("over_voice_list", gson.toJson(overList));
//                                    }
//                                    list.get(position).setFilestatus(true);
//                                    adapter.notifyDataSetChanged();
//                                }
//                            }
//                        });
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

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

            Map map = new HashMap();
            map.put("user_id",tel);
            map.put("record_ids",overList);
            JSONObject object = new JSONObject(map);
            RequestBody requestBody = RequestBody.create(HttpConst.JSON_MEDIA_TYPE, object.toString());

            Request request = new Request.Builder()
                    .url("http://app.xinheyidian.com/xinheyidian/ecg/record/query_records.do")
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
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    LogUtil.showLog("..onResponse.......json...." + json);
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONObject object = new JSONObject(json);

                            if (0 == object.getInt("status")) {

                            }
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = object;
                            handler.sendMessage(msg);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

    }

}
