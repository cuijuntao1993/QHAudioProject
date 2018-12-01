package com.gz.audio.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gz.audio.R;
import com.gz.audio.entiy.ECG_Records;
import com.gz.audio.ui.aniview.BaseProgressDialog;
import com.gz.audio.ui.aniview.mProgressDialog;
import com.gz.audio.ui.xindian.DemoView;
import com.gz.audio.utils.AudioRecorder;
import com.gz.audio.utils.BytesTransUtil;
import com.gz.audio.utils.DateUtil;
import com.gz.audio.utils.FileUtils;
import com.gz.audio.utils.KDSharedPreferences;
import com.gz.audio.utils.LogUtil;
import com.gz.audio.utils.RecordStreamListener_check;
import com.gz.audio.utils.SharePreferenceUtil;
import com.gz.audio.utils.TimeUtil;
import com.gz.audio.utils.ToastUtil;
import com.gz.audio.widget.TimerCountdownView;
import com.xinheyidian.FMDemod.EnhancedFilter;
import com.xinheyidian.FMDemod.QRSDetectorPan;
import com.xinheyidian.FMDemod.Utils;
import com.xinheyidian.FMDemod.ZeroCrossingDemod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

//import com.xinheyidian.FMDemod.QRSDetector;

/**
 * Created by Liuyz on 2018/5/22.
 */

public class CollectActivity extends BaseActivity {
    //    private com.gz.audio.CollectBinding binding;1122test up github
    private boolean isStart = false;
    private boolean isRecord = false;
    private boolean isback = false;
    AudioRecorder audioRecorder;
    private String fileName,pcmFilePath;
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
    private boolean bl_check = true;//check signal strength
    private boolean is_write = false;//start decode data
    private boolean found_ecg = false;//find ecg signal for the first time
    private long first_found_ecg_signal_time = 0;

    //心电图模拟
    DemoView demoView;
    ArrayList<Double> rawECG,dataList,tempList,resultList;
    int intt = 0;
    //tel
    private  String telephone;
    //loading
    private BaseProgressDialog hud;
    //实时解码sdk实例
    private ZeroCrossingDemod zrdmod;
    private EnhancedFilter eFilter;
//    private QRSDetector qrsDetector;
    private int fs = 300;
    //录制开始时间   录制结束时间
    private String start_time,end_time;
    private ECG_Records xd_double;
    private boolean is_ecg_start = false;
    //临时缓冲区buffer
    private ArrayList<Short> shorts_buffer = new ArrayList<>();
    int bf_count = 1;
    //建立流写入文件
    DataOutputStream fos = null;
    private int DECODE_STEP = 22050;
    private int MAX_QRS_CALC_LENGTH = 300 * 5;//max length to calculate QRS
    private Timer drawTimer;
    private TimerTask drawTask;
    private float xdpi;
    private float ydpi;

    private int signal_memory;
    private TextView signal_tv;
    private TextView tv_xin;
    private ImageView signal_img;
    //心率
    private String xinlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        zrdmod = new ZeroCrossingDemod();
        eFilter = new EnhancedFilter();
//        qrsDetector = new QRSDetector(fs);
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        telephone = sharedPreferences.getString("telephone", "test");
        audioRecorder = AudioRecorder.getInstance();
        // 更新UI线程
        mainHanlder = new Handler(Looper.getMainLooper());
        initView();
        new WorkThread().start();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        xdpi = metrics.xdpi;
        ydpi = metrics.ydpi;

    }



    private Handler preHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //信号合格,开启录音
                    initRecording();
                    demoView.setResolution(xdpi,ydpi);
                    break;
                case 2:
                    drawTimer = new Timer();
                    demoView.setResolution(xdpi,ydpi);
                    drawTask = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    demoView.setData(dataList);
                                    tv_xin.setText(xinlv);
                                }
                            });
                        }
                    };
                    drawTimer.schedule(drawTask,0,30);//start draw

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
                case 4:
                    switch (signal_memory){
                        case 1://较差
                            signal_tv.setText("较差");
                            signal_tv.setTextColor(Color.YELLOW);
                            signal_img.setImageResource(R.mipmap.in_01);
                            break;
                        case 2://较好
                            signal_tv.setText("较好");
                            signal_tv.setTextColor(Color.GREEN);
                            signal_img.setImageResource(R.mipmap.in_02);
                            break;
                        case 3://良好
                            signal_tv.setText("良好");
                            signal_tv.setTextColor(Color.GREEN);
                            signal_img.setImageResource(R.mipmap.in_03);
                            break;
                        case 0://无信号
                            signal_tv.setText("无信号");
                            signal_tv.setTextColor(Color.BLACK);
                            signal_img.setImageResource(R.mipmap.in_0);
                            break;
                    }
                    break;
                case 5:
                    timer.setText("結束");
                    showRestartDialog();
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
            fileName = start_time = TimeUtil.getCurrentTime(TimeUtil.TIME_FORMAT_TWO);
            audioRecorder.createDefaultAudio(fileName,telephone);

                String currentFileName = fileName;
                File file = new File(FileUtils.getPcmFileAbsolutePath(currentFileName,telephone));
                if (file.exists()) {
                    file.delete();
                }
            pcmFilePath = file.getAbsolutePath();
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
                            int quality = signal_quality(data);
//                            int quality = 2;
                            //显示信号强弱
                            if(quality != signal_memory){
                                signal_memory = quality;
                                Message msg = Message.obtain();
                                msg.what = 4;   //标志消息的标志
                                preHandler.sendEmptyMessage(msg.what);
                            }

                            if (quality >= 1 && bl_check) {
                                //从全局池中返回一个message实例，避免多次创建message（如new Message）
                                if(!found_ecg){
                                    first_found_ecg_signal_time = new Date().getTime();
                                    found_ecg = true;
                                }else{
                                    long time = new Date().getTime();
                                    if(time-first_found_ecg_signal_time>=1000){//if signal last for 1 second
                                        Log.d("test_cui_quality", ">2 is check ");
                                        Message msg = Message.obtain();
                                        msg.what = 1;   //标志消息的标志
                                        preHandler.sendEmptyMessage(msg.what);
                                        bl_check = false;
                                    }
                                }

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
                                found_ecg = false;
                                preHandler.removeMessages(1);
                                Log.d("test_cui_quality", "is remove ");
                                shorts_buffer = new ArrayList<>();

                            }else  if(quality < 1 && is_ecg_start){
                                Log.d("test_cui_quality", "is finish ");
                                isback = true;
                                //信号丢失判断
                                if(null!=timer.getText().toString()) {
                                    String[] sp = timer.getText().toString().split(":");
                                    if (sp.length > 1) {
                                        if (null != Integer.valueOf(sp[1])) {
                                            if (Integer.valueOf(sp[1]) <= 20) {
                                                //超过了10秒
                                                isRecord = false;
                                                is_write = false;
                                                stopTimer();
                                                countdownview.destroy();
                                                shorts_buffer = null;
                                                if (isRecord) {
                                                    //停止录音
                                                    audioRecorder.stopRecord();
                                                }

                                                end_time = TimeUtil.getCurrentTime(TimeUtil.TIME_FORMAT_TWO);
                                                if (fos != null) {
                                                    try {
                                                        fos.flush();
                                                        fos.close();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                Message msg = Message.obtain();
                                                msg.what = 5;   //标志消息的标志
                                                preHandler.sendEmptyMessage(msg.what);
                                            } else {
                                                //不到10秒
                                                Intent intent = getIntent();
                                                finishthisActivity();
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                }
                            }
                            curPos=0;
                            remainSize = end - (tmpByteBuffer.length-curPos);
                            srcStartPos += (tmpByteBuffer.length-curPos);
                        }
                        if(remainSize>0){
                            System.arraycopy(dataB,srcStartPos,tmpByteBuffer,curPos,remainSize);
                            curPos+=end;
                        }

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
                    if(shorts_buffer!=null) {
                        if (shorts_buffer.size() > DECODE_STEP*bf_count) {
                            //每次取DECODE_STEP个short进行解码
                            short[] s = new short[DECODE_STEP];
                            for (int i = 0; i < s.length; i++) {
                                s[i] = shorts_buffer.get(DECODE_STEP * (bf_count - 1) + i);
                            }

                            bf_count++;
                            double[] d = zrdmod.process(s);
                            if(d==null)//decode nothing out
                                continue;
                            for (int i = 0; i < d.length; i++) {
                                rawECG.add(d[i]);
                            }

                            int qrs_calc_length = Math.min(MAX_QRS_CALC_LENGTH,rawECG.size());
                            double[] qrsSeg = new double[qrs_calc_length];
                            int offset = rawECG.size()-qrs_calc_length;
                            for(int i =0;i<qrs_calc_length;i++){
                                qrsSeg[i]=rawECG.get(i+offset);
                            }
                            int[] qrs_loc = QRSDetectorPan.detect(qrsSeg);
                            for(int i=0;i<qrs_loc.length;i++){
                                qrs_loc[i]+=offset;
                            }

                            int heat_rate = RR_rate_cal(qrs_loc);//计算心率

                            if(heat_rate == -1){
                                xinlv = "--";
                            }else{
                                xinlv = String.valueOf(heat_rate);
                            }
                            //test
                            //Log.d("xinlv",heat_rate+"");



                            double[] cur_ecg = eFilter.process(d, qrs_loc);
                            if(cur_ecg == null)
                                continue;
                            for (int i = 0; i < cur_ecg.length; i++) {
                                dataList.add(cur_ecg[i]);
                            }
                        }
                    }
                }

            };
        }.start();
    }

    private int RR_rate_cal(int[] qrs_loc)
    {
        //RR rate calculation
        if(qrs_loc==null)
            return -1;
        double sum = 0;
        if(qrs_loc.length>=3)
        {
            for (int i = 0; i < qrs_loc.length - 1; i++) {
                sum = sum + (qrs_loc[i + 1] - qrs_loc[i]) / 300.0;
            }
            double RR_rate =60/( sum / (qrs_loc.length - 1));
            return (int)Math.round(RR_rate);
        }
        else
        {
            return -1;
        }

    }

    //信号判断
    public int signal_quality(short[] audio_data) {
        return Utils.cal_intensity(audio_data);
    }

    private void initView() {
        //模拟心电图准备
        dataList = new ArrayList<Double>();
        rawECG = new ArrayList<Double>();
        tempList=new ArrayList<Double>();
        resultList=new ArrayList<Double>();
        demoView = (DemoView) findViewById(R.id.demoview);

        // 录音钱准备倒计时
        isRecord = true;
        countdownview = findViewById(R.id.countdownview);
        back = findViewById(R.id.back);
        timer = findViewById(R.id.timer);
        signal_tv = findViewById(R.id.signal_tv);
        signal_img = findViewById(R.id.signal_img);
        tv_xin = findViewById(R.id.tv_xin);
        // 录音倒计时
        int time = SharePreferenceUtil.getInt("length", 30);
//        time = 10;
        countdownview.setMaxTime(time);
        // 后退
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finishthisActivity();
            }
        });
    }


    private void initRecording() {

        isRecord = true;
        is_write = true;
        is_ecg_start = true;
        if (!isStart) {

            //读取缓冲区数据开始画图
            getData_draw();
            showLoading("初始化中..");

            isStart = true;
            ToastUtil.showToast(context, "录音开始");
            // 开始倒计时
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    //execute the task
                    countdownview.updateView();
                    countdownview.addCountdownTimerListener(litener);
                }
            }, 2000);
            //1s后开始画图线程
            preHandler.sendEmptyMessageDelayed(2,2000);
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
                stopTimer();
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
                saveData();
            }
        });
    }
    private void saveData(){
        //保存图片
        FileUtils.saveViewToPic(demoView,"cuitest",telephone);
        String edfPath = FileUtils.saveDoubleToEdf(dataList,fileName,telephone);
        xd_double = new ECG_Records();
        xd_double.setPhoneNumber(telephone);//保存账户（tel）
        Bitmap bitmap = FileUtils.createViewBitmap(demoView);
        byte[] imgs = FileUtils.img(bitmap);
        xd_double.setXinDianByShort(imgs);//保存心电图片数据
        //保存基本数据
        xd_double.setRecord_ID(0);
        xd_double.setArchive_ID(0);
        xd_double.setStartTime(start_time);
        xd_double.setEndTime(end_time);
        xd_double.setDeviceType(0);
        xd_double.setLeadsType(1);
        xd_double.setAudioFilePath(pcmFilePath);
        xd_double.setRawEDFFilePath(edfPath);
        xd_double.setProcessedEDFFilePath("");
        xd_double.setState(0);
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
    private void showLoading(String msg) {
        hud = new mProgressDialog(this)
                .setLabel(msg);
        hud.setCancelable(false);
        hud.show();
        scheduleDismiss();
    }

    private void showRestartDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.mipmap.public_transparent_icon);
        window.setContentView(R.layout.public_save_dialog);

        TextView content = (TextView) window.findViewById(R.id.save_dialog_content);
        TextView cancel = (TextView) window.findViewById(R.id.save_dialog_cancel);
        TextView submit = (TextView) window.findViewById(R.id.save_dialog_submit);

        content.setText("采集信息少于30秒");
        cancel.setText("重测");
        submit.setText("保存");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = getIntent();
                finishthisActivity();
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                saveData();
            }
        });
    }

    File file;

    @Override
    protected void onPause() {
        super.onPause();
        if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_START) {
            audioRecorder.pauseRecord();
        }
    }

    private void finishthisActivity(){
        if(null!= hud){
            hud.dismiss();
        }
        stopTimer();
        countdownview.destroy();
        shorts_buffer = null;
        is_write = false;
        isRecord = false;
        if(isRecord){
            //停止录音
            audioRecorder.stopRecord();
        }
        if (fos != null) {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CollectActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        if(null!=hud){
            hud.dismiss();
        }
        super.onDestroy();
        shorts_buffer = null;
        is_write = false;
        isRecord = false;
        if(isRecord){
            //停止录音
            audioRecorder.stopRecord();
        }
        if (fos != null) {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isRecord) {
               finishthisActivity();
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
    private void stopTimer(){
        if (drawTimer != null) {
            drawTimer.cancel();
            drawTimer = null;
        }
    }
    private void uploadFile() {
        KDSharedPreferences editor = KDSharedPreferences.getInstence();
        final String authtoken = editor.getString("token","");
        int user_id = editor.getInt("id",0);

        file = new File(xd_double.getRawEDFFilePath());
        Log.d("short_length",shorts_buffer.size()+"");
        dialog = ProgressDialog.show(this, "", "音频上传中...", false, false);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("ecg_data", file.getName(), fileBody)
                .addFormDataPart("user_id",user_id+"")
                .addFormDataPart("Archive_ID", "0")
                .addFormDataPart("StartTime", start_time)
                .addFormDataPart("EndTime", end_time)
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
                //设置Auth
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        return response.request().newBuilder().header("Authorization", authtoken).build();
                    }
                })
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
}
