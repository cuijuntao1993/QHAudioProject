package com.gz.audio.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.gz.audio.R;
import com.gz.audio.ui.xindian.ECG_allData_View;
import com.gz.audio.ui.xindian.WH_ECGView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by boya on 2018/9/16.
 */

public class XD_InFormation extends BaseActivity implements View.OnTouchListener{

    private final Rect mRect = new Rect();
    private BitmapRegionDecoder mDecoder;
    private ImageView mView;
    private DisplayMetrics dm;
    private int screenHeight;
    private int screenWidth;
    //private int downX;
    private int downY;
    private int startY;
    private int showHeight;
    private int imgHeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xd_xq);
        mView = (ImageView) findViewById(R.id.blog_content);


        dm = new DisplayMetrics();

        mView = new ImageView(this);
        mView.setAdjustViewBounds(true);
        mView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mView.setScaleType(ImageView.ScaleType.CENTER);
        mView.setOnTouchListener(this);

        mView.post(new Runnable() {

            @Override
            public void run() {
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                screenHeight = dm.heightPixels;
                screenWidth = dm.widthPixels;

                mRect.set(0, 0, screenWidth, screenHeight);
                Bitmap bm = mDecoder.decodeRegion(mRect, null);
                mView.setImageBitmap(bm);

                showHeight = screenHeight;
                startY = 0;

                imgHeight = mDecoder.getHeight();

                //System.out.println(imgHeight);
                //System.out.println(mDecoder.getHeight());
                //System.out.println(mView.getWidth());
                //System.out.println(mView.getHeight());
            }
        });

        setContentView(mView);

        try {
            @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(R.drawable.longtest);
            mDecoder = BitmapRegionDecoder.newInstance(is, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction() & MotionEvent.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //downX = (int) event.getX();;
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //setImageRegion(x, y);
                int x = (int) event.getX();
                int y = (int) event.getY();

                int deltaY = downY - y;
                downY = y;

                System.out.println(showHeight);
                System.out.println(deltaY);
                if(showHeight <= screenHeight && deltaY < 0){
                    break;
                }else if(showHeight >= imgHeight + 500 && deltaY > 0){
                    //System.out.println("else if");
                    break;
                }
                showHeight += deltaY;
                startY += deltaY;
                mRect.set(0, startY, screenWidth, showHeight);
                Bitmap bm = mDecoder.decodeRegion(mRect, null);
                mView.setImageBitmap(bm);
                //System.out.println(deltaY);
                break;
        }
        return true;

    }


}
