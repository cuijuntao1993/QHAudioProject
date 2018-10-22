package com.gz.audio.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gz.audio.R;
import com.gz.audio.event.MsgEvent;
import com.gz.audio.utils.IntentUtil;
import com.gz.audio.utils.SharePreferenceUtil;
import com.gz.audio.utils.VoiceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Liuyz on 2018/5/22.
 */

public class SettingActivity extends BaseActivity {
//    private com.gz.audio.SettingBinding binding;

    TextView length;
    View back,layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_setting);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        EventBus.getDefault().register(this);

        initView();
        initListener();
    }


    private void initView() {
        length = findViewById(R.id.length);
        back = findViewById(R.id.back);
        layout = findViewById(R.id.layout);
        int time = SharePreferenceUtil.getInt("length", 30);
        length.setText(VoiceUtil.getVoiceLenght(time));
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(SettingActivity.this, SettingLengthActivity.class);
            }
        });
    }

    @Subscribe
    public void onEvent(MsgEvent event) {
        length.setText(event.msg + "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
