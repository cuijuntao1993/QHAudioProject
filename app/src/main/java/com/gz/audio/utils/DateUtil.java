package com.gz.audio.utils;

/**
 * Created by Liuyz on 2018/5/23.
 */

public class DateUtil {

    public static String showTheTimer(int seconds) {
        String timer = "";
        String sminute = "";
        String ssecond = "";
        if (seconds >= 0) {
            int minute = seconds / 60;
            int second = seconds % 60;

            if (minute < 10) {
                sminute = "0" + minute + ":";
            } else {
                sminute = minute + ":";
            }
            if (second < 10) {
                ssecond = "0" + second;
            } else {
                ssecond = second + "";
            }
            timer = sminute + ssecond;
        } else {
            timer = "00:00";
        }
        return timer;
    }

}
