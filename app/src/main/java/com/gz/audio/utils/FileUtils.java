package com.gz.audio.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import com.xinheyidian.FMDemod.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HXL on 16/8/11.
 * 管理录音文件的类
 */
public class FileUtils {


    public static String rootPath = "QHCollectID";
    //原始文件(不能播放)
    public final static String AUDIO_PCM_BASEPATH = "/" + rootPath + "/pcm/";
    //可播放的高质量音频文件
    public final static String AUDIO_WAV_BASEPATH = "/" + rootPath + "/wav/";
    //地址弃用,改为灵活输入文件名+文件夹名

    public static String getPcmFileAbsolutePath(String fileName,String direcName) {
        if (TextUtils.isEmpty(fileName)) {
            throw new NullPointerException("fileName isEmpty");
        }
        if (!isSdcardExit()) {
            throw new IllegalStateException("sd card no found");
        }
        String mAudioRawPath = "";
        if (isSdcardExit()) {
            if (!fileName.endsWith(".pcm")) {
                fileName = fileName + ".pcm";
            }
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + rootPath+"/"+direcName + "/pcm/";
            File file = new File(fileBasePath);
            //创建目录
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioRawPath = fileBasePath + fileName;
        }

        return mAudioRawPath;
    }

    public static String getWavFileAbsolutePath(String fileName,String direcName) {
        if (fileName == null) {
            throw new NullPointerException("fileName can't be null");
        }
        if (!isSdcardExit()) {
            throw new IllegalStateException("sd card no found");
        }

        String mAudioWavPath = "";
        if (isSdcardExit()) {
            if (!fileName.endsWith(".wav")) {
                fileName = fileName + ".wav";
            }
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"  + rootPath+"/"+direcName + "/wav/";
            File file = new File(fileBasePath);
            //创建目录
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioWavPath = fileBasePath + fileName;
        }
        return mAudioWavPath;
    }

    public static String getEdfFileAbsolutePath(String fileName,String direcName) {
        if (fileName == null) {
            throw new NullPointerException("fileName can't be null");
        }
        if (!isSdcardExit()) {
            throw new IllegalStateException("sd card no found");
        }

        String mAudioWavPath = "";
        if (isSdcardExit()) {
            if (!fileName.endsWith(".edf")) {
                fileName = fileName + ".edf";
            }
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"  + rootPath+"/"+direcName + "/edf/";
            File file = new File(fileBasePath);
            //创建目录
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioWavPath = fileBasePath + fileName;
        }
        return mAudioWavPath;
    }
    /**
     * 判断是否有外部存储设备sdcard
     *
     * @return true | false
     */
    public static boolean isSdcardExit() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 获取全部pcm文件列表
     *
     * @return
     */
    public static List<File> getPcmFiles() {
        List<File> list = new ArrayList<>();
        String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_PCM_BASEPATH;

        File rootFile = new File(fileBasePath);
        if (!rootFile.exists()) {
        } else {

            File[] files = rootFile.listFiles();
            for (File file : files) {
                list.add(file);
            }

        }
        return list;

    }

    /**
     * 获取全部wav文件列表
     *
     * @return
     */
    public static List<File> getWavFiles() {
        List<File> list = new ArrayList<>();
        String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_WAV_BASEPATH;

        File rootFile = new File(fileBasePath);
        if (!rootFile.exists()) {
        } else {
            File[] files = rootFile.listFiles();
            for (File file : files) {
                list.add(file);
            }

        }
        return list;
    }

    public static String getMD5Method(File file) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi.toString(16);
    }

    public static void saveViewToPic(View view,String name,String tel) {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"  + "/" + rootPath+"/"+tel+"/pic/";
        File baseFile = new File(path);
        //创建目录
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
        File file = new File(path, name + ".png");  //指定我们要保存到那个文件中
        try {
            file.createNewFile();
            Bitmap bitmap = createViewBitmap(view);
            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveDoubleToEdf(ArrayList<Double> doubleArrayList,String name,String tel) {
        int edf_length = doubleArrayList.size()/300*300;
        double[] edf_data = new double[edf_length];
        for(int i = 0;i<edf_length;i++){
            edf_data[i] = doubleArrayList.get(i);
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"  + "/" + rootPath+"/"+tel+"/edf/";
        File baseFile = new File(path);
        //创建目录
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }
        File file = new File(path, name + ".edf");  //指定我们要保存到那个文件中
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            Utils.writeEDF(outputStream,edf_data,new Date(), "1", true, new Date(), "jerry", edf_length/300);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap createViewBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    public static byte[]img(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

}
