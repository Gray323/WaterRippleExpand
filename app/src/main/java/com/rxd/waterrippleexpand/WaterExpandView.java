package com.rxd.waterrippleexpand;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/2/2.
 */

public class WaterExpandView extends View{

    private Paint mPaint;
    private int mScreenWidth;//屏幕宽度
    private int mScreenHeight;//屏幕高度
    private int mCircleWidth;//圆的半径
    private volatile int mStrokeWidth;//画笔的宽度
    private volatile int speed = 5;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    int arg1 = msg.arg1;
                    mPaint.setStrokeWidth(arg1);
                    invalidate();
                    break;
                default:
                    break;
            }
        }
    };

    public WaterExpandView(Context context) {
        this(context, null);
    }

    public WaterExpandView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterExpandView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        mCircleWidth = (int) Math.sqrt(mScreenHeight * mScreenHeight + mScreenWidth * mScreenWidth);
        Log.d("TAG", "mCircleWidth = " + mCircleWidth);
        mStrokeWidth  = (mCircleWidth - 200) * 2;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        setBackground(getResources().getDrawable(R.mipmap.example));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mScreenWidth / 2, mScreenHeight / 2, mCircleWidth, mPaint);
        if (mStrokeWidth > mCircleWidth){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(50);
                        Message message = new Message();
                        message.what = 1;
                        mStrokeWidth -= speed;
                        speed += 5;
                        Log.d("TAG", "mStrokeWidth = " + mStrokeWidth);
                        message.arg1 = mStrokeWidth;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
