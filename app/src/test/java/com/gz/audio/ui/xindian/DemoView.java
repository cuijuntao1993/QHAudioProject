package com.gz.audio.ui.xindian;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.gz.audio.R;

public class DemoView extends View {
	private Paint paint;
	private int mBorderColor;
	private float mBorderWidth;
	private ArrayList<Double> dataList = null;
	private float xdpi;
	private float ydpi;
	private int ecg_sample_rate = 300;//ecg采样率300Hz
	private int time_resolution = 25;//时间分辨率25mm/s
	private int amptitude_resolution = 10;//幅值分辨率 10mm/mv
	private int display_end_pos = 0;
	private int points_per_draw = 30 * 300 /1000;//每隔30ms画一次
	float inch_per_mm = 0.03937008f;//1mm等于0.03937008inch
	float mm_per_inch = 25.4f;//1inch等于25.4mm
	private Paint mPaint;
	private Paint backgroudPaint;
	private Paint backgroudBoldPaint;
	private float plotInterval = 3.5f;
	private int discard_points_num = 0;//don't display the first second data

	

	public DemoView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		dataList=new ArrayList<Double>();
		mPaint = new Paint();
		mPaint.setColor(Color.rgb(255, 0, 0));// 画笔为color
		mPaint.setStrokeWidth(1);// 设置画笔粗细
		mPaint.setAntiAlias(true);
		mPaint.setFilterBitmap(true);
		mPaint.setStyle(Paint.Style.FILL);
		backgroudPaint = new Paint();
		backgroudPaint.setColor(Color.rgb(239, 239, 239));// 画笔为color
		backgroudPaint.setStrokeWidth(1);// 设置画笔粗细
		backgroudPaint.setAntiAlias(true);
		backgroudPaint.setFilterBitmap(true);
		backgroudPaint.setStyle(Paint.Style.FILL);
		backgroudBoldPaint = new Paint();
		backgroudBoldPaint.setColor(Color.rgb(239, 239, 239));// 画笔为color
		backgroudBoldPaint.setStrokeWidth(2);// 设置画笔粗细
		backgroudBoldPaint.setAntiAlias(true);
		backgroudBoldPaint.setFilterBitmap(true);
		backgroudBoldPaint.setStyle(Paint.Style.FILL);
	}

	public DemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		final TypedArray attributes = context.getTheme()
				.obtainStyledAttributes(attrs, R.styleable.DemoView,
						0, 0);
		try {
			mBorderColor = attributes.getColor(
					R.styleable.DemoView_border_color, 0);
			mBorderWidth = attributes.getDimension(
					R.styleable.DemoView_border_width, 0);
		} finally {
			attributes.recycle();
		}
		dataList=new ArrayList<Double>();
		mPaint = new Paint();
		mPaint.setColor(Color.rgb(0, 0, 0));// 画笔为color
		mPaint.setStrokeWidth(1);// 设置画笔粗细
		mPaint.setAntiAlias(true);
		mPaint.setFilterBitmap(true);
		mPaint.setStyle(Paint.Style.FILL);
		backgroudPaint = new Paint();
		backgroudPaint.setColor(Color.rgb(239, 239, 239));// 画笔为color
		backgroudPaint.setStrokeWidth(1);// 设置画笔粗细
		backgroudPaint.setAntiAlias(true);
		backgroudPaint.setFilterBitmap(true);
		backgroudPaint.setStyle(Paint.Style.FILL);
		backgroudBoldPaint = new Paint();
		backgroudBoldPaint.setColor(Color.rgb(239, 239, 239));// 画笔为color
		backgroudBoldPaint.setStrokeWidth(2);// 设置画笔粗细
		backgroudBoldPaint.setAntiAlias(true);
		backgroudBoldPaint.setFilterBitmap(true);
		backgroudBoldPaint.setStyle(Paint.Style.FILL);
	}

	public DemoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		dataList=new ArrayList<Double>();

	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(dataList.size()==0)
			return;
		//draw background grid

		//Draw vertical lines
		int count = 0;
		int canvasWidth = getWidth();
		int canvasHeight = getHeight();
		float xStepInPixel = inch_per_mm*xdpi;
		float yStepInPixel = inch_per_mm*ydpi;
		for(float startXline=xStepInPixel;startXline<canvasWidth;startXline+=xStepInPixel){
			if(count%5==4){
				canvas.drawLine(startXline,0,startXline,canvasHeight,backgroudBoldPaint);
			}else{
				canvas.drawLine(startXline,0,startXline,canvasHeight,backgroudPaint);
			}
			count++;
		}
		count = 0;
		for(float startYline=canvasHeight/2;startYline<canvasHeight;startYline+=yStepInPixel){
			if(count%5==0){
				canvas.drawLine(0,startYline,canvasWidth,startYline,backgroudBoldPaint);
			}else{
				canvas.drawLine(0,startYline,canvasWidth,startYline,backgroudPaint);
			}
			count++;
		}
		count = 0;
		for(float startYline=canvasHeight/2;startYline>0;startYline-=yStepInPixel){
			if(count%5==0){
				canvas.drawLine(0,startYline,canvasWidth,startYline,backgroudBoldPaint);
			}else{
				canvas.drawLine(0,startYline,canvasWidth,startYline,backgroudPaint);
			}
			count++;
		}

		int display_points_num = (int) Math.ceil(getWidth() / xdpi *mm_per_inch/this.time_resolution*this.ecg_sample_rate);
		int interval_points_num = (int) Math.ceil(plotInterval/this.time_resolution*this.ecg_sample_rate);
		int baseLine = getHeight()/2;
		display_end_pos += points_per_draw;//当前绘制的数据终点
		if (display_end_pos>dataList.size()){//确保数据不越界
			display_end_pos = dataList.size();
		}


//		start_x=0;
//		start_y=0;
//		end_x=getWidth();
//		end_y=getHeight();
//		canvas.drawLine(start_x,start_y,end_x,end_y,mPaint);
		float start_x,start_y,end_x,end_y;
		int start_index ;
		if(display_end_pos <= display_points_num){//未满一个扫描周期
			float[] draw_points = new float[(display_end_pos-1)*4];
			start_x = 0;
			start_y = baseLine - dataList.get(0+discard_points_num).floatValue()*amptitude_resolution*inch_per_mm*ydpi;
			canvas.drawPoint(start_x,start_y,mPaint);
			for(int i=0;i<display_end_pos-1;i++){
				draw_points[i*4] = start_x;
				draw_points[i*4+1] = start_y;
				end_x = (i+1)*1.0f/ecg_sample_rate*time_resolution*inch_per_mm*xdpi;
				end_y = baseLine - dataList.get(i+1+discard_points_num).floatValue()*amptitude_resolution*inch_per_mm*ydpi;
				draw_points[i*4+2]=end_x;
				draw_points[i*4+3]=end_y;
				start_x=end_x;
				start_y = end_y;
			}
			canvas.drawLines(draw_points,mPaint);
		}else{ //exceed an period
			int circle_pos = display_end_pos % display_points_num;
			if(circle_pos + interval_points_num <= display_points_num){
				start_index=0;
			}else{
				start_index = (circle_pos + interval_points_num)%display_points_num;
			}
			start_x = start_index * 1.0f/ecg_sample_rate*time_resolution*inch_per_mm*xdpi;
			start_y = baseLine - dataList.get(display_end_pos-circle_pos+start_index+discard_points_num).floatValue()*amptitude_resolution*inch_per_mm*ydpi;
			canvas.drawPoint(start_x,start_y,mPaint);
			if(circle_pos > 1){
				float[] draw_points_pre = new float[(circle_pos-start_index-1)*4];//扫描线前半段
				for(int i=start_index;i< circle_pos-1;i++){
					draw_points_pre[(i-start_index)*4] = start_x;
					draw_points_pre[(i-start_index)*4+1] = start_y;
					end_x = (i+1)*1.0f/ecg_sample_rate*time_resolution*inch_per_mm*xdpi;
					end_y = baseLine - dataList.get(display_end_pos-circle_pos+i+1+discard_points_num).floatValue()*amptitude_resolution*inch_per_mm*ydpi;
					draw_points_pre[(i-start_index)*4+2]=end_x;
					draw_points_pre[(i-start_index)*4+3]=end_y;
					start_x=end_x;
					start_y = end_y;
				}
				canvas.drawLines(draw_points_pre,mPaint);
			}
			if(display_points_num > circle_pos+interval_points_num+1) {
				float[] draw_points_after = new float[(display_points_num - circle_pos - interval_points_num - 1) * 4];//扫描线后半段
				start_x=(circle_pos+interval_points_num)*1.0f/ecg_sample_rate*time_resolution*inch_per_mm*xdpi;
				start_y = baseLine - dataList.get(display_end_pos-display_points_num+interval_points_num+discard_points_num).floatValue()*amptitude_resolution*inch_per_mm*ydpi;
				for (int i = 0; i < display_points_num - circle_pos- interval_points_num - 1; i++) {
					draw_points_after[i * 4] = start_x;
					draw_points_after[i * 4 + 1] = start_y;
					end_x = (circle_pos+interval_points_num+i+1) * 1.0f / ecg_sample_rate * time_resolution * inch_per_mm * xdpi;
					end_y = baseLine - dataList.get(display_end_pos-display_points_num+interval_points_num+i+1+discard_points_num).floatValue() * amptitude_resolution * inch_per_mm * ydpi;
					draw_points_after[i * 4 + 2] = end_x;
					draw_points_after[i * 4 + 3] = end_y;
					start_x = end_x;
					start_y = end_y;
				}
				canvas.drawLines(draw_points_after, mPaint);
			}
		}

//		float heiht=getHeight()/(float)2000;
//        Log.e("height", dataList.size()+"");
//		float width=getWidth()/(float)1000;
//        double data1=0,data2=0;
//		paint = new Paint();
//		paint.setColor(mBorderColor);
//		paint.setStrokeWidth(mBorderWidth);
//		paint.setAntiAlias(true);
//		paint.setStyle(Style.STROKE);
//		paint.setAntiAlias(true);
//		if (dataList.size()>1) {
//			for (int i = 1; i < dataList.size(); i++) {
//				data1=dataList.get(i-1)*10000;
//				data2=dataList.get(i)*10000;
//				canvas.drawLine(i*width,(float)(getHeight()-(data1-1000)*heiht), (i+1)*width, (float) (getHeight()-(data2-1000)*heiht), paint);
//			}
//		}


	}

	public void setResolution(float xdpi,float ydpi){
		this.xdpi=xdpi;
		this.ydpi=ydpi;
	}
	public void setData(ArrayList<Double> data) {
		this.dataList = data;
		invalidate();
	}

}
