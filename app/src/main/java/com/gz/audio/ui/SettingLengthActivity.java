package com.gz.audio.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gz.audio.R;
import com.gz.audio.adapter.SettingLengthAdapter;
import com.gz.audio.entiy.VoiceLengthBean;
import com.gz.audio.event.MsgEvent;
import com.gz.audio.interfaces.OnViewItemListener;
import com.gz.audio.utils.SharePreferenceUtil;
import com.gz.audio.utils.VoiceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liuyz on 2018/5/22.
 */

public class SettingLengthActivity extends BaseActivity implements OnViewItemListener {
//    private com.gz.audio.SettingLengthBinding binding;
    private SettingLengthAdapter adapter;
    private List<VoiceLengthBean> list = new ArrayList<>();
    private VoiceLengthBean bean;

    RecyclerView recylerview;
    View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_setting_length);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_length);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        //购房助手adapter
        recylerview = findViewById(R.id.recylerview);
        back = findViewById(R.id.back);
        recylerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SettingLengthAdapter(this, list, this);
        recylerview.setAdapter(adapter);
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if (position >= 0) {
            SharePreferenceUtil.putInt("length", list.get(position).getFilelength());
            EventBus.getDefault().post(new MsgEvent(VoiceUtil.getVoiceLenght(list.get(position).getFilelength())));
        }
        finish();
    }

    private void initData() {
        list.clear();
        int time = SharePreferenceUtil.getInt("length", 30);
        list.add(new VoiceLengthBean(30, time == 30 ? true : false));
        list.add(new VoiceLengthBean(60, time == 60 ? true : false));
        list.add(new VoiceLengthBean(120, time == 120 ? true : false));
        list.add(new VoiceLengthBean(180, time == 180 ? true : false));
        list.add(new VoiceLengthBean(240, time == 240 ? true : false));
        list.add(new VoiceLengthBean(300, time == 300 ? true : false));
        adapter.notifyDataSetChanged();
    }
}
