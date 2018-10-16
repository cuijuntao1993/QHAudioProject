package com.gz.audio.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Liuyz on 2018/5/22.
 */

/**
 * 默认是浅绿色
 *
 * @author lq
 */
public class TimerCountdownView extends View {

    int mMaxSeconds = 0;
    float mRateAngle = 0;
    private float mMaxAngle = 0;

    /**
     * 外圈相关
     **/
    private float mOutCircleWidth = 8;
    private int mOutCircleColor = 0xff00b492;
    //起始角度
    private float mOutStartAngle = -90;
    //扫描角度
    private float mOutSweepAngle = -360;


    /**
     * 内圈相关
     **/
    private float mInCircleWidth = 8;
    private int mInCircleColor = 0xffddd6d6;
    //起始角度
    private float mInStartAngle = -90;
    //扫描角度
    private float mInSweepAngle = -360;

    /**
     * 外圈与内圈的距离
     **/
    private float mOutAndInPadding = 2; //外援环和小圆环虹之间的间隔

    //发起重回命令
    private int mActionHeartbeat = 1;

    //间隔时间
    private int mDelayTime = 1 * 1000;

    private CountdownTimerListener mListener;

    public TimerCountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == mActionHeartbeat) {
                mMaxAngle = mMaxAngle - mRateAngle;
                mMaxSeconds = mMaxSeconds - 1;
                if (mMaxSeconds >= 0) {
                    invalidate();
                    mHandler.sendEmptyMessageDelayed(mActionHeartbeat, mDelayTime);
                    if (mListener != null) {
                        mListener.onCountDown(mMaxSeconds);
//                        mListener.onCountDown(showTheTimer(mMaxSeconds));
                        mListener.onTimeArrive(false);
                    }
//                  Log.d("", "剩余"+mMaxSeconds+"秒" +"  剩余角度："+mMaxAngle);
                } else {
                    mListener.onTimeArrive(true);
                }
            }
        }

        ;
    };

    public void updateView() {
        mHandler.sendEmptyMessage(mActionHeartbeat);
    }

    public void destroy() {
        mHandler.removeMessages(100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawInCircle(canvas);
        drawOutCircle(canvas);
    }

    public void drawInCircle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mInCircleColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(60);
        paint.setStrokeWidth(mInCircleWidth);
        float left = mOutAndInPadding + mOutCircleWidth;
        float top = left;
        float right = getWidth() - left;
        float bottom = getHeight() - top;
        RectF oval = new RectF(left, top, right, bottom);
        canvas.drawArc(oval, mInStartAngle, mInSweepAngle, false, paint);

    }


    public void drawOutCircle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mOutCircleColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(80);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mOutCircleWidth);

        float left = 1;
        float top = left;
        float right = getWidth() - left;
        float bottom = getHeight() - top;
        RectF oval = new RectF(left + mOutCircleWidth, top + mOutCircleWidth, right - mOutCircleWidth, bottom - mOutCircleWidth);

        canvas.drawArc(oval, mOutStartAngle, mMaxAngle, false, paint);


    }


    /**
     * 设置初始最大时间
     *
     * @param minute 单位分
     */
    public void setMaxTime(int minute) {
        mMaxAngle = mOutSweepAngle;
        mMaxSeconds = minute;
        mRateAngle = mMaxAngle / mMaxSeconds;
    }

    public void setOutCicleColor(int color) {
        mOutCircleColor = color;
    }

    public void setOutCicleWidth(int width) {
        mOutCircleWidth = width;
    }

    public void setInCicleColor(int color) {
        mInCircleColor = color;
    }

    public void setInCicleWidth(int width) {
        mInCircleWidth = width;
    }

    public void setOuterAndInerPadding(int padding) {
        mOutAndInPadding = padding;
    }


    public interface CountdownTimerListener {

        //当前倒计时计算的文本  格式 mm-ss
        public void onCountDown(int time);
//        public void onCountDown(String time);

        //倒计时是否到达
        public void onTimeArrive(boolean isArrive);
    }

    public void addCountdownTimerListener(CountdownTimerListener listener) {
        mListener = listener;
    }
}
