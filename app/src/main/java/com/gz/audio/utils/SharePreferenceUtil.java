package com.gz.audio.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 程序配置文件（读取和写入）
 * <p>
 * Created by zhihui_chen on 14-8-4.
 */
public class SharePreferenceUtil {

    /**
     * context
     */
    private static Context context = BaseContext.getContext();


    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 写入配置信息，需要最后面进行 commit()
     *
     * @param key
     * @param value
     * @return
     */
    public static void putString(String key, String value) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 写入配置信息，需要最后面进行 commit()
     *
     * @param key
     * @param value
     * @return
     */
    public static void putInt(String key, int value) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 写入配置信息，需要最后面进行 commit()
     *
     * @param key
     * @param value
     * @return
     */
    public static void putLong(String key, long value) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 写入配置信息，需要最后面进行 commit()
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static void putBoolean(Activity context, String key, boolean value) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 读取配置信息
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key, boolean def) {
        return getSharedPreferences().getBoolean(key, def);
    }

    /**
     * 读取配置信息
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    /**
     * 读取配置信息
     *
     * @param key
     * @return
     */
    public static int getInt(String key, int def) {
        return getSharedPreferences().getInt(key, def);
    }

    /**
     * 读取配置信息
     *
     * @param key
     * @return
     */
    public static long getLong(String key, long def) {
        return getSharedPreferences().getLong(key, def);
    }

    /**
     * 本地是否保存有该值
     *
     * @param key
     * @return
     */
    public static boolean containsKey(String key) {
        return getSharedPreferences().contains(key);
    }

    /**
     * 删除配置信息，可以同时删除多个
     *
     * @param keys
     */
    public static void remove(String... keys) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.commit();
    }

    /**
     * 清除所有配置文件
     */
    public static void clearMarkCheckChace(String name) {
        SharedPreferences sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.clear();
        edit.commit();
    }

}
