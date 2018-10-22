package com.gz.audio.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.gz.audio.R;
import com.gz.audio.ui.MainActivity;

import java.io.File;


/**
 * @author liufuning
 *         <p>
 *         create time:2013-11-14上午9:24:12
 */
public final class IntentUtil {

    /**
     * 重启
     */
    public static void restartApp(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("key", "restart");
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Activity之间跳转
     *
     * @param activity
     * @param cls
     */
    public static void startActivity(Activity activity, Class<?> cls) {
        startActivity(activity, cls, null);
    }

    public static void startActivity(Context activity, Class<?> cls) {
        startActivity((Activity) activity, cls, null);
    }

    /**
     * Activity之间跳转
     *
     * @param activity
     * @param cls
     * @param extras
     */
    public static void startActivity(Activity activity, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void startLeftActivity(Activity activity, Class<?> cls, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);

        if (extras != null) {
            intent.putExtras(extras);
        }

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * Activity之间跳转
     *
     * @param activity
     * @param cls
     * @param extras
     */
    public static void startActivityForResult(Activity activity, Class<?> cls,
                                              int requestCode, Bundle extras) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);

        if (extras != null) {
            intent.putExtras(extras);
        }

        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    public static void startActivityForResult(Activity activity, Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    /**
     * 获取打系统浏览器Intent
     *
     * @return
     */
    public static Intent getSystemBrowserIntent(String url) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
        return intent;
    }

    /**
     * 获取打电话Intent
     *
     * @param phoneNum 电话号码
     * @return
     */
    public static Intent getCallIntent(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                + phoneNum));
        return intent;
    }

    /**
     * 跳转到打电话界面
     *
     * @param phoneNum
     * @return
     */
    public static Intent getDialIntent(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + phoneNum));
        return intent;
    }

    /**
     * 获取启动相机的Intent对象
     *
     * @return
     */
    public static Intent getOpenCameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent getOpenCameraIntent(Uri uri) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.75); // 默认设置为高质量的图片：1-0质量一直递减

        return intent;
    }

    /**
     * 获取打开相册的Intent对象
     *
     * @return
     */
    public static Intent getOpenAlbumIntent() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        return intent;
    }

    /**
     * 获取裁剪图片的Intent对象,裁剪后的图片大小为110px*100px
     *
     * @param uri
     * @return
     */
    public static Intent getCropImageIntent(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 110);
        intent.putExtra("outputY", 110);
        intent.putExtra("return-data", true);

        return intent;
    }

    /**
     * 获取裁剪图片的Intent对象,裁剪后的图片大小为110px*100px
     *
     * @param data
     * @return
     */
    public static Intent getCropImageIntent(Bitmap data) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", data);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 110);
        intent.putExtra("outputY", 110);
        intent.putExtra("return-data", true);

        return intent;
    }

    /**
     * 获取安装APK的Intent
     *
     * @param uri
     * @return
     */
    public static Intent getInstallAPKIntent(Uri uri) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 新开辟一个栈
        installIntent.setDataAndType(uri,
                "application/vnd.android.package-archive");
        return installIntent;
    }

    /**
     * 打开系统相册浏览照片
     *
     * @param uri
     * @return
     */
    public static Intent getBrowsePictureIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * 打开文件通用方法
     *
     * @param filePath
     * @return
     */
    public static Intent openFile(String filePath) {

        File file = new File(filePath);
        if (!file.exists())
            return null;
        /* 取得扩展名 */
        String end = file
                .getName()
                .substring(file.getName().lastIndexOf(".") + 1,
                        file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else {
            return getAllIntent(filePath);
        }
    }

    /**
     * Android获取一个用于打开APK文件的intent
     */
    public static Intent getAllIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    /**
     * Android获取一个用于打开APK文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * Android获取一个用于打开VIDEO文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    /**
     * Android获取一个用于打开AUDIO文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    /**
     * Android获取一个用于打开Html文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    /**
     * Android获取一个用于打开图片文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    /**
     * Android获取一个用于打开PPT文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    /**
     * Android获取一个用于打开Excel文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    /**
     * Android获取一个用于打开Word文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    /**
     * Android获取一个用于打开CHM文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    /**
     * Android获取一个用于打开文本文件的intent
     *
     * @param param
     * @param paramBoolean
     * @return
     */
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    /**
     * Android获取一个用于打开PDF文件的intent
     *
     * @param param
     * @return
     */
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    /**
     * 分享文本到系统应用
     *
     * @return
     */

    public static void shareTextToSystem(Context context, String title,
                                         String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, title + "   " + text);
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 分享图片到系统应用
     *
     * @return
     */

    public static void shareImageToSystem(Context context, String title,
                                          String url) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("output", Uri.fromFile(new File(url)));
        intent.putExtra("outputFormat", "JPEG");
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 包名
     *
     * @param appPackageName
     */
    public static void startAPP(Context context, String appPackageName) {
        try {
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "没有安装", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 包名
     *
     * @param appPackageName
     */
/*	public static void startAPP(Context context, String appPackageName,
            int result, String token) {
		try {
			ComponentName componentName = new ComponentName(appPackageName, "com.liveaa.yifudao.MainActivity");  
			Intent intent = new Intent();
			intent.setComponent(componentName);
			intent.putExtra("result", result);
			intent.putExtra("token", token);
			context.startActivity(intent);  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
    public static void startAPP(Context context, String appPackageName,
                                int result, String token) {
        try {
            Log.d("FFFFF", "appPackageName:" + appPackageName);
            Intent intent = context.getPackageManager()
                    .getLaunchIntentForPackage(appPackageName);
            intent.putExtra("result", result);
            intent.putExtra("token", token);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 包名
     *
     * @param appPackageName
     */
    public static void startAPP(Context context, String appPackageName,
                                String startActivity, String token, String type) {
        try {
            Intent intent = new Intent();
            intent.putExtra("type", type);
            intent.putExtra("token", token);
            ComponentName comp = new ComponentName(appPackageName, startActivity);
            intent.setComponent(comp);
            intent.setAction("android.intent.action.LAUNCHER");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 包名
     *
     * @param appPackageName
     */
    public static void startAPP(Context context, String appPackageName,
                                String startActivity, int result, String token) {
        try {
            Intent intent = new Intent();
            intent.putExtra("result", result);
            intent.putExtra("token", token);
            ComponentName comp = new ComponentName(appPackageName,
                    startActivity);
            intent.setComponent(comp);
            intent.setAction("android.intent.action.LAUNCHER");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 包名
     *
     * @param appPackageName
     */
    public static void startAPPType8(Context context, String appPackageName,
                                     String startActivity, int result, String token, String circleId) {
        try {
            Intent intent = new Intent();
            intent.putExtra("result", result);
            intent.putExtra("type", "8");
            intent.putExtra("token", token);
            intent.putExtra("circleId", circleId);
            ComponentName comp = new ComponentName(appPackageName,
                    startActivity);
            intent.setComponent(comp);
            intent.setAction("android.intent.action.LAUNCHER");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 包名
     *
     * @param appPackageName
     */
    public static void startAPPType(Context context, String appPackageName,
                                    String startActivity, int result, String type, String token,
                                    String circleId) {
        try {
            Intent intent = new Intent();
            intent.putExtra("result", result);
            intent.putExtra("type", type);
            intent.putExtra("token", token);
            if (type.equals("8")) {
                intent.putExtra("circleId", circleId);
            } else {
                intent.putExtra("postId", circleId);
            }
            ComponentName comp = new ComponentName(appPackageName,
                    startActivity);
            intent.setComponent(comp);
            intent.setAction("android.intent.action.LAUNCHER");
            context.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 包名
     *
     * @param appPackageName
     */
    public static boolean haveInstallApp(Context context, String appPackageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(appPackageName, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 包名
     */
    public static boolean haveInstallApp2(Context context, String uri) {
        PackageInfo packageInfo = null;
        Intent intent = new Intent();
        Uri muri = Uri.parse(uri);
        intent.setData(muri);
        if (context.getPackageManager().resolveActivity(intent, 0) == null) {

            return false;
        }
        return true;
    }

}
