package com.gz.audio.ecgdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gz.audio.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Timer;
import java.util.TimerTask;

public class EcgActivity extends Activity {
    private Timer mTimerECG = new Timer();
    private TimerTask mTaskECG;
    private Handler mHandlerECG;
    private String title = "ECG";
    private XYSeries mSeries;
    private XYMultipleSeriesDataset mDataset;
    private GraphicalView mChartECG;
    private XYMultipleSeriesRenderer mRenderer;
    private Context mContext;
    private int mECGX = -100, mECGY;
    private int mECGDataIndex = 0;
    int[] mECGBackX = new int[100];
    int[] mECGBackY = new int[100];


    public float mECGDataArr[] = new float[]{
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            0.3f,
            2.3f,
            2.4f,
            1.3f,
            -2.5f,
            2.5f,
            -2.5f,
            3.5f,
            2.5f,
            2.0f,
            1.5f,
            1.7f,
            2.1f,
            -3.5f,
            2.8f
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecg_activity);
        mContext = getApplicationContext();
        LinearLayout layout_ecg = (LinearLayout)findViewById(R.id.ll_ecg);
        mSeries = new XYSeries(title);
        mDataset = new XYMultipleSeriesDataset();
        mDataset.addSeries(mSeries);

        int color = Color.rgb(0, 0, 0);

        PointStyle style = PointStyle.POINT;
        mRenderer = buildRenderer(color, style, false);
        setChartSettings(mRenderer, "X", "Y", 0, 100, -3.5, 3.5, Color.WHITE, Color.WHITE);


//        mRenderer.setMargins(new int[]{0, 0, 0, 0});//设置空白区大小
//        mRenderer.setMarginsColor(Color.TRANSPARENT);//设置空白区颜色

        //生成图表
        mChartECG = ChartFactory.getLineChartView(mContext, mDataset, mRenderer);

        //将图表添加到布局中去
        layout_ecg.addView(mChartECG, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //这里的Handler实例将配合下面的Timer实例，完成定时更新图表的功能
        mHandlerECG = new Handler() {
            @Override
            public void handleMessage(Message msg)
            {
                //刷新ECG图
                updateEcgCure();
                super.handleMessage(msg);
            }
        };
        mTaskECG = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                mHandlerECG.sendMessage(message);
                //message.what = 2;

            }
        };

        mTimerECG.schedule(mTaskECG, 100, 100);
    }

    protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        //设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(color);
        r.setPointStyle(style);
        r.setFillPoints(fill);
        r.setLineWidth(3);
        renderer.addSeriesRenderer(r);

        return renderer;
    }
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle,
                                    double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        //有关对图表的渲染可参看api文档
        //renderer.setChartTitle(title);
        //renderer.setXTitle(xTitle);
        //renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(Color.rgb(201, 114, 78));
        renderer.setShowAxes(false);
        renderer.setShowLabels(false);
        renderer.setShowGrid(true);
        renderer.setShowCustomTextGrid(false);
        renderer.setGridColor(Color.rgb(78, 78, 78));
        //renderer.setXLabels(20);
        //renderer.setYLabels(10);
        //renderer.setXTitle("Time");
        //renderer.setYTitle("dBm");
        renderer.setChartTitleTextSize(0);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setPointSize((float) 2);
        renderer.setShowLegend(false);
        renderer.setMargins(new int[]{0, 0, 0, 0});//设置空白区大小
        renderer.setMarginsColor(Color.TRANSPARENT);//设置空白区颜色
    }
    private void updateEcgCure() {

        //设置好下一个需要增加的节点
        mECGX = 0;
        //addY = (int)(Math.random() * 90);
        mECGY = (int)((mECGDataArr[mECGDataIndex]));
        mECGDataIndex+=4;


        //移除数据集中旧的点集
        mDataset.removeSeries(mSeries);

        //判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100
        int length = mSeries.getItemCount();
        if (length > 100) {
            length = 100;
        }

        //将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
        for (int i = 0; i < length; i++) {
            mECGBackX[i] = (int) mSeries.getX(i) + 1;
            mECGBackY[i] = (int) mSeries.getY(i);
        }

        //点集先清空，为了做成新的点集而准备
        mSeries.clear();

        //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
        //这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
        mSeries.add(mECGX, mECGY);
        for (int k = 0; k < length; k++) {
            mSeries.add(mECGBackX[k], mECGBackY[k]);
        }

        //在数据集中添加新的点集
        mDataset.addSeries(mSeries);

        //视图更新，没有这一步，曲线不会呈现动态
        //如果在非UI主线程中，需要调用postInvalidate()，具体参考api
        mChartECG.invalidate();
        if(mECGDataIndex>=mECGDataArr.length){
            mECGDataIndex = 0;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimerECG != null) {
            mTimerECG.cancel();
        }
    }
}
