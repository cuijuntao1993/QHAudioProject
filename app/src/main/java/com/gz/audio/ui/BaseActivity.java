package com.gz.audio.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Window;

import com.google.gson.Gson;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.gz.audio.R;

/**
 * Created by Liuyz on 2017/9/4.
 */
public abstract class BaseActivity extends FragmentActivity {

    protected static DisplayMetrics metric;
    protected Activity context;
    protected Context mContext;
    protected int screenWidth;
    protected int screenHeight;
    protected Gson gson = new Gson();
    protected ImmersionBar mImmersionBar;

    /**
     * 用于统计页面访问路径
     * 如果页面是使用Tab+ViewPager+Fragment实现的, 则在实现类Activity中设为false, 由Fragment统计页面访问路径
     */
    protected boolean isAnalytics = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        context = this;
        mContext = this;
        if (metric == null) {
            metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
        }
        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();

        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.hideBar(BarHide.FLAG_SHOW_BAR).statusBarDarkFont(true).init();
        mImmersionBar
                .navigationBarColor(R.color.black)
//                .fullScreen(true)
                .statusBarDarkFont(true)
                .statusBarColor(R.color.top_bar_color)
                .addTag(this.getClass().getName()+"")  //给上面参数打标记，以后可以通过标记恢复
                .hideBar(BarHide.FLAG_SHOW_BAR).init();
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
    }
}
