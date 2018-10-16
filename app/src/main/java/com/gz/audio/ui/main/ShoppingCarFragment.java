package com.gz.audio.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gz.audio.R;

public class ShoppingCarFragment extends BaseFragment {


    public ShoppingCarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shoping_car, container, false);
        return view;
    }

}
