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
//        uploadByCheck();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


}
