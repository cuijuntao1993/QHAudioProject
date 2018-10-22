package com.gz.audio.utils;

/**
 * Created by Liuyz on 2018/5/22.
 */

public class VoiceUtil {
    public static String getVoiceLenght(int length) {
        if (length <= 0) {
            return "";
        }

        String s = "";
        if (length < 60) {
            s = length + "s";
        } else {
            if (length % 60 == 0) {
                s = (int) (length / 60) + "min";
            } else {
                s = (int) (length / 60) + "min" + (length % 60) + "s";
            }
        }
        return s;
    }

    public static String showVoiceLenght(int length) {
        if (length <= 0) {
            return "";
        }

        String s = "";
        if (length <= 32) {
            s = "30s";
        } else if (length <= 62) {
            s = "1min";
        } else if (length <= 122) {
            s = "2min";
        } else if (length <= 182) {
            s = "3min";
        } else if (length <= 242) {
            s = "4min";
        } else if (length <= 302) {
            s = "5min";
        } else {
            s = (int) (length / 60) + "min" + (length % 60) + "s";
        }
        return s;
    }
}
