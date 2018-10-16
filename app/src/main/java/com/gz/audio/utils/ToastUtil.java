package com.gz.audio.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	private static Toast toast;
	/**
	 * Toast提醒
	 * 
	 * @param context
	 * @param msg
	 */
	@SuppressLint("WrongConstant")
	public static void showToast(Context context, String msg) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}


}
