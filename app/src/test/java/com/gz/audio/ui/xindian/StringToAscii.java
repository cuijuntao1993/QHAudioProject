package com.gz.audio.ui.xindian;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wenh on 16/10/2114:01.
 */

public class StringToAscii {
    private static String toHexUtil(int n){
        String rt="";
        switch(n){
            case 10:rt+="A";break;
            case 11:rt+="B";break;
            case 12:rt+="C";break;
            case 13:rt+="D";break;
            case 14:rt+="E";break;
            case 15:rt+="F";break;
            default:
                rt+=n;
        }
        return rt;
    }

    public static String toHex(int n){
        StringBuilder sb=new StringBuilder();
        if(n/16==0){
            return toHexUtil(n);
        }else{
            String t=toHex(n/16);
            int nn=n%16;
            sb.append(t).append(toHexUtil(nn));
        }
        return sb.toString();
    }
    public static String parseAscii(String str){
        StringBuilder sb=new StringBuilder();
        byte[] bs=str.getBytes();
        for(int i=0;i<bs.length;i++)
            sb.append(toHex(bs[i]));
        return sb.toString();
    }

    public static ArrayList<String> test(String args){
        ArrayList<String> data_arr = new ArrayList<String>();

        Pattern p = Pattern.compile("\\d{3,}");//这个3是指连续数字的最少个数
        Matcher m = p.matcher(args);
        int i = 0;
        while (m.find()) {
//            Log.v("json","****  -- > " + m.group());
            data_arr.add(m.group());
            i++;
        }
//        Log.v("json","****" + i + "  " + data_arr.toString());
        return data_arr;
    }

}
