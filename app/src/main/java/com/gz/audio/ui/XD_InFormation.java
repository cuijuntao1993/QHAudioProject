package com.gz.audio.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gz.audio.R;
import com.gz.audio.entiy.ECG_Records;
import com.gz.audio.ui.xindian.ECG_allData_View;
import com.gz.audio.ui.xindian.WH_ECGView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by boya on 2018/9/16.
 */

public class XD_InFormation extends BaseActivity {
    private WebView wb_img;
    private TextView info_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xd_xq);

        wb_img = (WebView) findViewById(R.id.wb_img);
        info_text = (TextView) findViewById(R.id.info_text);

        String url = "";
        wb_img .loadUrl("file:///android_res/mipmap/home_banner.png");
        WebSettings settings = wb_img.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(false); //选择内置缩放机制与单独缩放控件；
        settings.setDisplayZoomControls(false); //是否显示缩放控件
        settings.setSupportZoom(false);  //是否支持缩放

        settings.setLoadWithOverviewMode(true);
        //允许webview对文件的操作
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);

        wb_img.getSettings().setJavaScriptEnabled(true);//启用js
        wb_img.getSettings().setBlockNetworkImage(false);//解决图片不显示
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        Intent intent = getIntent();
        ECG_Records records = (ECG_Records) intent.getSerializableExtra("user");
        info_text.setText(records.getDiagnose_details());

    }
}
