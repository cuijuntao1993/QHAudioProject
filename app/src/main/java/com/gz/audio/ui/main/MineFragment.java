package com.gz.audio.ui.main;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gz.audio.R;
import com.gz.audio.ui.ContactActivity;
import com.gz.audio.ui.LogoutActivity;
import com.gz.audio.utils.IntentUtil;


public class MineFragment extends BaseFragment {

    TextView tv_account;
    View view;
    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view =inflater.inflate(R.layout.fragment_mine,container,false);
        tv_account = view.findViewById(R.id.tv_account);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences( getActivity().getPackageName(), Context.MODE_PRIVATE);
        String telephone = sharedPreferences.getString("telephone", "");
        tv_account.setText(telephone);
        view.findViewById(R.id.tv_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getActivity(), ContactActivity.class);
            }
        });
        view.findViewById(R.id.tv_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getActivity(), LogoutActivity.class);
            }
        });
        return view;
    }

}
