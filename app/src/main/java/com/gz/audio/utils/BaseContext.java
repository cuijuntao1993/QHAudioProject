package com.gz.audio.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Liuyz on 2017/9/4.
 */
public class BaseContext {
    private static Context context;

    public static void init(Application application){
        context = application.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
