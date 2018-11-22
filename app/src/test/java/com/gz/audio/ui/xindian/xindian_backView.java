package com.gz.audio.ui.xindian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.gz.audio.R;

import java.util.ArrayList;

/**
 * Created by boya on 2018/9/13.
 */

public class xindian_backView  extends View {

    private float gap_grid;//网格间距
    private int width,height;//本页面宽，高
    private int xori;//原点x坐标
    private int grid_hori,grid_ver;//横、纵线条数
    private float gap_x;//两点间横坐标间距
    private int dataNum_per_grid = 18;//每小格内的数据个数
    private float y_center;//中心y值
    private ArrayList<Float> data_source;


    public xindian_backView(Context context, AttributeSet attrs){
        super(context,attrs);
        //背景色
        this.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public xindian_backView(Context context){
        super(context);
        //背景色
        this.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed){

//            Log.e("json","两点间横坐标间距:" + gap_x + "   矩形区域两点间横坐标间距：" + rect_gap_x);
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        xori = 0;
        gap_grid = 30.0f;
        width = getWidth();
        height = getHeight();
        grid_hori = height/(int)gap_grid;
        grid_ver = width/(int)gap_grid;
        gap_x = gap_grid/dataNum_per_grid;
        Log.e("json","本页面宽： " + width +"  高:" + height);
        DrawGrid(canvas);
    }

    /**
     * 画背景网格
     */
    private void DrawGrid(Canvas canvas){
        //横线
        for (int i = 1 ; i < grid_hori + 2 ; i ++){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getResources().getColor(R.color.gainsboro)); //<color name="data_pr">#0a7b14</color>
            paint.setStrokeWidth(1.0f);
            Path path = new Path();
            path.moveTo(xori, gap_grid * (i-1) + (height-grid_hori*gap_grid)/2);
            path.lineTo(width,gap_grid * (i-1) + (height-grid_hori*gap_grid)/2);
            if ( i % 5 != 0 ){//每第五条，为实线   其余为虚线 ，以下为画虚线方法
                PathEffect effect = new DashPathEffect(new float[]{1,5},1);
                paint.setPathEffect(effect);
            }
            canvas.drawPath(path,paint);
        }
        //竖线
        for (int i = 1 ; i < grid_ver + 2 ; i ++){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getResources().getColor(R.color.limegreen));
            paint.setStrokeWidth(1.0f);
            Path path = new Path();
            path.moveTo(gap_grid * (i-1) + (width-grid_ver*gap_grid)/2, 0);
            path.lineTo(gap_grid * (i-1) + (width-grid_ver*gap_grid)/2,height);
            if ( i % 5 != 0 ){
                PathEffect effect = new DashPathEffect(new float[]{1,5},1);
                paint.setPathEffect(effect);
            }
            canvas.drawPath(path,paint);
        }
    }

    /**
     * 暴露接口，设置数据源
     */
    public void setData(ArrayList<Float> data){
        this.data_source = data;
        invalidate();
    }

}