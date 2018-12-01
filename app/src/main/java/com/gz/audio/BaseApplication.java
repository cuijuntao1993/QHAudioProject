package com.gz.audio;

import android.app.Application;

import com.gz.audio.utils.BaseContext;

import org.litepal.LitePalApplication;

/**
 * Created by Liuyz on 2017/8/29.
 */
public class BaseApplication extends LitePalApplication {

    public static int time = 1000;
    private static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        BaseContext.init(this); // 初始化上下文
    }

    public static BaseApplication getApplication() {
//		AppLog.printI("App", "getApplication");
        return application;
    }
}
