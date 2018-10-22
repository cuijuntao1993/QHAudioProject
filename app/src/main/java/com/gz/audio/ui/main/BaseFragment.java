package com.gz.audio.ui.main;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;


public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
    }

}
