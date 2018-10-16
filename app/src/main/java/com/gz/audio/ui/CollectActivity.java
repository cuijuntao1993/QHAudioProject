package com.gz.audio.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.gz.audio.R;
import com.gz.audio.entiy.ECG_Records;
import com.gz.audio.ui.aniview.BaseProgressDialog;
import com.gz.audio.ui.aniview.mProgressDialog;
import com.gz.audio.ui.xindian.DemoView;
import com.gz.audio.ui.xindian.xindian_backView;
import com.gz.audio.utils.AudioRecorder;
import com.gz.audio.utils.BytesTransUtil;
import com.gz.audio.utils.DateUtil;
import com.gz.audio.utils.FileUtils;
import com.gz.audio.utils.LogUtil;
import com.gz.audio.utils.RecordStreamListener_check;
import com.gz.audio.utils.SharePreferenceUtil;
import com.gz.audio.utils.TimeUtil;
import com.gz.audio.utils.ToastUtil;
import com.gz.audio.widget.TimerCountdownView;
import com.xinheyidian.FMDemod.FMDemod;
import com.xinheyidian.FMDemod.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Timer;
import java.util.TimerTask;
import static java.lang.Thread.sleep;

/**
 * Created by Liuyz on 2018/5/22.
 */

public class CollectActivity extends BaseActivity {
    //    private com.gz.audio.CollectBinding binding;
    private boolean isStart = false;
    private boolean isRecord = false;
    private boolean isback = false;
    AudioRecorder audioRecorder;
    private String fileName;
    private Handler mainHanlder;
    private ProgressDialog dialog;
    private int status = 1; // 0.成功  1.失败
    private String msg = ""; //记录返回数据
    private String overVoiceMsg;
    private List<String> overList = null; // 记录已上传文件名称
    private byte[] tmpByteBuffer = new byte[4096*2];
    private int curPos = 0;

    private TimerCountdownView countdownview;
    private View back;
    TextView timer;
    private boolean bl_check = true;
    private boolean is_write = false;

    //心电图模拟
    private xindian_backView ecgView;
    DemoView demoView;
    ArrayList<Double> dataList,tempList,resultList;
    int intt = 0;
    //tel
    private  String telephone;
    //loading
    private BaseProgressDialog hud;
    //实时解码sdk实例
    private FMDemod fmDemod;
    //录制开始时间   录制结束时间
    private String start_time,end_time;
    ECG_Records xd_double;
    private boolean is_ecg_start = false;
    //临时缓冲区buffer
    private ArrayList<Short> shorts_buffer = new ArrayList<>();
    int bf_count = 1;
    //建立流写入文件
    DataOutputStream fos = null;
    private int DECODE_STEP = 16384;
    private Timer drawTimer;
    private TimerTask drawTask;
    private float xdpi;
    private float ydpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        fmDemod = new FMDemod();
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        telephone = sharedPreferences.getString("telephone", "18612789735");
        audioRecorder = AudioRecorder.getInstance();
        // 更新UI线程
        mainHanlder = new Handler(Looper.getMainLooper());
        initView();
        new WorkThread().start();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        drawTimer = new Timer();
        drawTask = new TimerTask() {
            @Override
            public void run() {
                demoView.setData(dataList);
            }
        };
    }





    private Handler preHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //信号合格,开启录音
                    showLoading("初始化中..");
                    initRecording();
                    break;
                case 2:
                    try {
                        //间隔30毫秒
                        sleep(1000);
                        } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    drawTimer.schedule(drawTask,0,30);//start draw
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            while (is_write) {
//                                //获取9个点
//                                for (int index = 0; index < 9; index++) {
//                                    //实时获取信号
//                                    if (tempList.size() == 0 && dataList.size() > 0) {
//                                        tempList.add(dataList.get(intt));
//                                    } else if (dataList.size() > 0) {
//                                        tempList.add(0, dataList.get(intt));
//                                    }
//                                    intt = intt + 1;
//                                    preHandler.sendEmptyMessage(2);
//                                    try {
//                                        //间隔30毫秒
//                                        sleep(30);
//                                    } catch (InterruptedException e) {
//                                        // TODO Auto-generated catch block
//                                        e.printStackTrace();
//                                    }
//
//                                }
//                                //画图（待改动）
//                                demoView.setData(tempList);
//                            }
//                        }
//                    }).start();
                    break;
                case 3:
                    drawTimer.cancel();//stop draw
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        if(jsonObject.getString("msg").equals("上传成功")){
                            Toast.makeText(CollectActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            //更新本地表
                            String Record_ID = jsonObject.getString("Record_ID");
                            xd_double.setRecord_ID(Integer.valueOf(Record_ID));
                            xd_double.setState(1);
                            xd_double.save();
                        }else{
                            Toast.makeText(CollectActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //循环找寻信号线程
    public class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            /**
             耗时操作,找寻信号
             */
            //初始化录音
            fileName = start_time = TimeUtil.getCurrentTime(TimeUtil.TIME_FORMAT_21);
            audioRecorder.createDefaultAudio(fileName,telephone);

                String currentFileName = fileName;
                File file = new File(FileUtils.getPcmFileAbsolutePath(currentFileName,telephone));
                if (file.exists()) {
                    file.delete();
                }
            try {
                fos = new DataOutputStream(new FileOutputStream(file));// 建立一个可存取字节的文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            audioRecorder.startRecord_Check(new RecordStreamListener_check() {
                @Override
                public void recordOfByte(byte[] dataB, int begin, final int end) {
                    if (dataB != null && end > 0) {
                        int remainSize = end;
                        int srcStartPos = 0;
                        while (curPos+remainSize >= tmpByteBuffer.length){
                            System.arraycopy(dataB,srcStartPos,tmpByteBuffer,curPos,tmpByteBuffer.length-curPos);
                            short[] data = BytesTransUtil.getInstance().Bytes2Shorts(tmpByteBuffer);
                            //int quality = signal_quality(data);
                            int quality = 1;
                            if (quality >= 1 && bl_check) {
                                //从全局池中返回一个message实例，避免多次创建message（如new Message）
                                bl_check = false;
                                Log.d("test_cui_quality", ">2 is check ");
                                Message msg = Message.obtain();
                                msg.what = 1;   //标志消息的标志
                                preHandler.sendEmptyMessage(msg.what);
                                //preHandler.sendEmptyMessageDelayed(msg.what,1000);

                            }else if(quality >= 1 && is_write){
                                for (int i = 0; i < data.length;i++){
                                    shorts_buffer.add(data[i]);
                                }
                                try {
                                    fos.write(tmpByteBuffer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else if(quality < 1 && !is_ecg_start){
                                bl_check = true;
                                preHandler.removeMessages(1);
                                Log.d("test_cui_quality", "is remove ");
                                shorts_buffer = new ArrayList<>();

                            }else  if(quality < 1 && is_ecg_start){
                                Log.d("test_cui_quality", "is finish ");
                                isback = true;
                                audioRecorder.stopRecord();
                                CollectActivity.this.finish();
                            }
                            curPos=0;
                            remainSize = end - (tmpByteBuffer.length-curPos);
                            srcStartPos += (tmpByteBuffer.length-curPos);
                        }
                        if(remainSize>0){
                            System.arraycopy(dataB,srcStartPos,tmpByteBuffer,curPos,remainSize);
                            curPos+=end;
                        }
//                        if((curPos+end)<tmpByteBuffer.length){//if accumulate data less than 4096*2
//                            System.arraycopy(dataB,0,tmpByteBuffer,curPos,end);
//                            curPos += end;
//                        }else{
//                            //把byte转为short
//                            System.arraycopy(dataB,0,tmpByteBuffer,curPos,tmpByteBuffer.length-curPos);
//                            short[] data = BytesTransUtil.getInstance().Bytes2Shorts(dataB);
//                            //信号持续策略
//                            //int quality = signal_quality(data);
//                            int quality = 1;
//                            if (quality >= 1 && bl_check) {
//                                //从全局池中返回一个message实例，避免多次创建message（如new Message）
//                                bl_check = false;
//                                Log.d("test_cui_quality", ">2 is check ");
//                                Message msg = Message.obtain();
//                                msg.what = 1;   //标志消息的标志
//                                preHandler.sendEmptyMessage(msg.what);
//                                //preHandler.sendEmptyMessageDelayed(msg.what,1000);
//
//                            }else if(quality >= 1 && is_write){
//                                for (int i = 0; i < data.length;i++){
//                                    shorts_buffer.add(data[i]);
//                                }
//                                try {
//                                    fos.write(dataB);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }else if(quality < 1 && !is_ecg_start){
//                                bl_check = true;
//                                preHandler.removeMessages(1);
//                                Log.d("test_cui_quality", "is remove ");
//                                shorts_buffer = new ArrayList<>();
//
//                            }else  if(quality < 1 && is_ecg_start){
//                                Log.d("test_cui_quality", "is finish ");
//                                isback = true;
//                                audioRecorder.stopRecord();
//                                CollectActivity.this.finish();
//                            }
//
//                        }
                    }
                }
            });

        }
    }

    private void getData_draw(){
        new Thread() {
            public void run() {

                //线程获取画图数据，并放入 dataList
                while (is_write){
                    if(shorts_buffer!=null && shorts_buffer.size() > DECODE_STEP*bf_count){
                        //每次取16317个short进行解码
                        short[] s = new short[DECODE_STEP];
                        for (int i = 0;i<s.length;i++){
                            s[i] = shorts_buffer.get(DECODE_STEP*(bf_count-1)+i);
                        }
                        bf_count ++;
                        double[] d = fmDemod.fm_demodulation(s);
                        for(int i = 0; i< d.length;i++){
                            dataList.add(d[i]);
                        }
                    }

                }

            };
        }.start();
    }

    //信号判断
    public int signal_quality(short[] audio_data) {
        return Utils.cal_intensity(audio_data);
    }

    private void initView() {
        //模拟心电图准备
        dataList = new ArrayList<Double>();
        tempList=new ArrayList<Double>();
        resultList=new ArrayList<Double>();
        demoView = (DemoView) findViewById(R.id.demoview);

        // 录音钱准备倒计时
        isRecord = true;
        countdownview = findViewById(R.id.countdownview);
        ecgView = (xindian_backView) findViewById(R.id.ecg_data_ecgView);
        back = findViewById(R.id.back);
        timer = findViewById(R.id.timer);
        // 录音倒计时
        int time = SharePreferenceUtil.getInt("length", 30);
//        time = 10;
        countdownview.setMaxTime(time);
        // 后退
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecord) {
                    ToastUtil.showToast(context, "录音过程中不可退出");
//                    showStopDialog();
                } else {
                    finish();
                }
            }
        });
    }


    private void initRecording() {
        isRecord = true;
        is_write = true;
        is_ecg_start = true;
        if (!isStart) {
            isStart = true;
            ToastUtil.showToast(context, "录音开始");
            // 开始倒计时
            countdownview.updateView();
            countdownview.addCountdownTimerListener(litener);
            //读取缓冲区数据开始画图
            getData_draw();
            //1s后开始画图线程
            //preHandler.sendEmptyMessageDelayed(2,1000);
        }
    }

    // 录音倒计时
    TimerCountdownView.CountdownTimerListener litener = new TimerCountdownView.CountdownTimerListener() {

        @Override
        public void onCountDown(int time) {
            timer.setTextSize(28);
            timer.setText(DateUtil.showTheTimer(time));
        }

        @Override
        public void onTimeArrive(boolean isArrive) {
            if (isArrive && !isback) {
                isRecord = false;
                is_write = false;
                //停止录音
                audioRecorder.stopRecord();
                timer.setText("結束");
                Toast.makeText(CollectActivity.this, "录音結束", Toast.LENGTH_SHORT).show();
                end_time = TimeUtil.getCurrentTime(TimeUtil.TIME_FORMAT_TWO);
                showSavedialog(FileUtils.getWavFileAbsolutePath(fileName,telephone));
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    //音频保存
    private void showSavedialog(final String path) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.mipmap.public_transparent_icon);
        window.setContentView(R.layout.public_save_dialog);

        TextView cancel = (TextView) window.findViewById(R.id.save_dialog_cancel);
        TextView submit = (TextView) window.findViewById(R.id.save_dialog_submit);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //保存图片
                FileUtils.saveViewToPic(demoView,"cuitest",telephone);
                FileUtils.saveDoubleToEdf(dataList,fileName,telephone);
                xd_double = new ECG_Records();
                xd_double.setPhoneNumber(telephone);
                xd_double.setIs_uploaded(false);
                Bitmap bitmap = FileUtils.createViewBitmap(demoView);
                byte[] imgs = FileUtils.img(bitmap);
                xd_double.setXinDianByShort(imgs);
                //保存基本数据
                xd_double.setArchive_ID(0);
                xd_double.setStartTime(start_time);
                xd_double.setEndTime(end_time);
                xd_double.setDeviceType(0);
                xd_double.setLeadsType(1);
                xd_double.setFilePath(FileUtils.getPcmFileAbsolutePath(fileName,telephone));
                xd_double.setFilePath("0");
                xd_double.setDiagnose_abstract("");
                xd_double.setDiagnose_details("");
                xd_double.setNote("");
                if(xd_double.save()){
                    Toast.makeText(CollectActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    uploadFile();
                }else{
                    Toast.makeText(CollectActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void showLoading(String msg) {
        hud = new mProgressDialog(this)
                .setLabel(msg);
        hud.setCancelable(false);
        hud.show();
        scheduleDismiss();
    }

    private void save_Edf(ArrayList<Double> doubleArrayList){
        double[] edf_data = new double[doubleArrayList.size()];
        for(int i = 0;i<doubleArrayList.size();i++){
            edf_data[i] = doubleArrayList.get(i);
        }

    }

    private void showStopDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.mipmap.public_transparent_icon);
        window.setContentView(R.layout.public_save_dialog);

        TextView content = (TextView) window.findViewById(R.id.save_dialog_content);
        TextView cancel = (TextView) window.findViewById(R.id.save_dialog_cancel);
        TextView submit = (TextView) window.findViewById(R.id.save_dialog_submit);

        submit.setText("取得取消录音退出?");
        cancel.setText("不退出");
        submit.setText("退出");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                isback = true;
                audioRecorder.stopRecord();
                File file = new File(FileUtils.getWavFileAbsolutePath(fileName,telephone));
                if (file.exists()) {
                    file.delete();
                }
                finish();
            }
        });
    }

    File file;

    private void uploadFile() {
        file = new File(FileUtils.getEdfFileAbsolutePath(fileName,telephone));
        Log.d("short_length",shorts_buffer.size()+"");
        dialog = ProgressDialog.show(this, "", "音频上传中...", false, false);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ecg_data", file.getName(), fileBody)
                .addFormDataPart("user_id", FileUtils.getMD5Method(file))
                .addFormDataPart("Archive_ID", "0")
                .addFormDataPart("StartTime", FileUtils.getMD5Method(file))
                .addFormDataPart("EndTime", FileUtils.getMD5Method(file))
                .addFormDataPart("DeviceType","0")
                .addFormDataPart("LeadsType", "1")
                .addFormDataPart("Note","")
                .build();
        Request request = new Request.Builder()
                .url("http://app.xinheyidian.com/xinheyidian/ecg/record/upload_file.do")
                .post(requestBody)
                .build();

        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.showLog("..onFailure....." + e.toString());
                mainHanlder.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        ToastUtil.showToast(context, "上传失败!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                dialog.dismiss();
                final String json = response.body().string();
                LogUtil.showLog("..onResponse.......json...." + json);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject object = new JSONObject(json);
                        if (object.has("status")) {
                            status = object.getInt("status");
                        }
                        if (object.has("msg")) {
                            msg = object.getString("msg");
                        }
                        if (status == 0) {
                            //成功返回数据
                            Message msg = new Message();
                            msg.what = 3;
                            msg.obj = object;
                            preHandler.sendMessage(msg);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_START) {
            audioRecorder.pauseRecord();
        }
    }

    @Override
    protected void onDestroy() {
        audioRecorder.release();
        hud.dismiss();
        super.onDestroy();
        shorts_buffer = null;
        is_write = false;
        isRecord = false;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isRecord) {
                ToastUtil.showToast(context, "录音过程中不可退出");
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    //loading back
    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hud.isShowing()) {
                    Log.d("dsf", "close");
                    hud.dismiss();
                }
            }
        }, 2000);
    }
}
