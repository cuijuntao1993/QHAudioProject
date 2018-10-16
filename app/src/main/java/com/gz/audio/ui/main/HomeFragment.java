package com.gz.audio.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gz.audio.R;
import com.gz.audio.ui.CollectActivity;
import com.gz.audio.ui.HistoryActivity;
import com.gz.audio.ui.MainActivity;
import com.gz.audio.ui.SettingActivity;
import com.gz.audio.utils.IntentUtil;


public class HomeFragment extends BaseFragment {

    public HomeFragment() {
    }
    View setting,history,collect;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view =inflater.inflate(R.layout.activity_main,container,false);
        initView();
        initListener();
        return view;
    }
    private void initView() {
        setting = view.findViewById(R.id.setting);
        history = view.findViewById(R.id.history);
        collect = view.findViewById(R.id.collect);
    }

    private void initListener() {
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getActivity(), SettingActivity.class);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getActivity(), HistoryActivity.class);
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getActivity(), CollectActivity.class);
            }
        });
    }

}
