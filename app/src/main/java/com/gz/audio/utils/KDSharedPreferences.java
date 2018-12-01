package com.gz.audio.utils;

import java.util.Set;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.gz.audio.BaseApplication;

public class KDSharedPreferences {

	private SharedPreferences mSPref;
	private static String SPREF_NAME="qh_spref";
	private static volatile KDSharedPreferences instence;

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static KDSharedPreferences getInstence() {
		if (instence == null) {
			synchronized(KDSharedPreferences.class)
			{
				if(instence == null){
					instence = new KDSharedPreferences();
				}
			}
		}
		return instence;
	}

	/**
	 * 构造方法实例化sp实例
	 */
	private KDSharedPreferences() {
		mSPref = BaseApplication.getApplication().getSharedPreferences(SPREF_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * 存储boolean类型
	 * 
	 * @param key
	 * @param value
	 */
	public void putBooleanValue(String key, boolean value) {
		Editor editor = mSPref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 存储int类型
	 * 
	 * @param key
	 * @param value
	 */
	public void putIntValue(String key, int value) {
		Editor editor = mSPref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * float
	 *
	 * @param key
	 * @param value
	 */
	public void putFloatValue_x(String key, float value) {
		Editor editor = mSPref.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	public void putFloatValue_y(String key, float value) {
		Editor editor = mSPref.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	public void putFloatValue_w(String key, float value) {
		Editor editor = mSPref.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	public void putFloatValue_h(String key, float value) {
		Editor editor = mSPref.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	/**
	 * 存储long类型
	 * 
	 * @param key
	 * @param value
	 */
	public void putLongValue(String key, long value) {
		Editor editor = mSPref.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 存储String类型
	 * 
	 * @param key
	 * @param value
	 */
	public void putStringValue(String key, String value) {
		Editor editor = mSPref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 存储set类型
	 * 
	 * @param key
	 * @param value
	 */
	public void putSetValue(String key, Set<String> value) {
		Editor editor = mSPref.edit();
		editor.putStringSet(key, value);
		editor.commit();
	}

	/**
	 * 获取boolean类型
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
	public boolean getBoolean(String key, boolean defValue) {
		return mSPref.getBoolean(key, defValue);
	}

	/**
	 * 获取int类型
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
	public int getInt(String key, int defValue) {
		return mSPref.getInt(key, defValue);
	}
	/**
	 * 获取float类型
	 *
	 * @param key
	 * @param defValue
	 * @return
	 */
	public float getFloat(String key, float defValue) {
		return mSPref.getFloat(key, defValue);
	}
	/**
	 * 获取long类型
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
	public Long getLong(String key, long defValue) {
		return mSPref.getLong(key, defValue);
	}

	/**
	 * 获取String类型
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
	public String getString(String key, String defValue) {
		return mSPref.getString(key, defValue);
	}

	/**
	 * 获取set类型
	 * 
	 * @param key
	 * @param defValues
	 * @return
	 */
	public Set<String> getStringSet(String key, Set<String> defValues) {
		return mSPref.getStringSet(key, defValues);
	}

}
