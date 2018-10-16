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
	
	

	public DemoView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		dataList=new ArrayList<Double>();
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

		float heiht=getHeight()/(float)2000;
        Log.e("height", dataList.size()+"");
		float width=getWidth()/(float)1000;
        double data1=0,data2=0;
		paint = new Paint();
		paint.setColor(mBorderColor);
		paint.setStrokeWidth(mBorderWidth);
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		if (dataList.size()>1) {
			for (int i = 1; i < dataList.size(); i++) {
				data1=dataList.get(i-1)*10000;
				data2=dataList.get(i)*10000;
				canvas.drawLine(i*width,(float)(getHeight()-(data1-1000)*heiht), (i+1)*width, (float) (getHeight()-(data2-1000)*heiht), paint);
			}
		}


	}

	
	public void setData(ArrayList<Double> data) {
		this.dataList = data;
		invalidate();
	}

}
