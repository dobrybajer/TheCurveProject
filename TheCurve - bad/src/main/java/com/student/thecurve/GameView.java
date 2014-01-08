package com.student.thecurve;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;


public class GameView extends View implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Display mDisplay;
    private Bitmap mBitmapPoint;
    private GameView gv;

    private float mHorizontalBound;
    private float mVerticalBound;

    private float mXOrigin;
    private float mYOrigin;

    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;

    private Player mPlayer;
    private Logic mLogic;

    private long mStartTime = 0;

    Handler mTimerHandler = new Handler();
    public Runnable mTimerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - mStartTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            if(gv!=null)
            {
                gv.postInvalidate();
            }
            mTimerHandler.postDelayed(this, 10);
        }
    };

    public GameView(Context context) {
        super(context);

        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mPlayer = new Player(Color.GREEN, "Kamil");
        mLogic = new Logic((Activity)this.getContext());
        mLogic.setPlayersOnStart(mPlayer);

        this.setBackgroundColor(Color.BLACK);

        gv = this;
        this.setDrawingCacheEnabled(true);
        mBitmapPoint = Bitmap.createBitmap(mDisplay.getWidth(), mDisplay.getHeight(),
                Bitmap.Config.ARGB_8888);
        //Toast.makeText(context, String.valueOf(mDisplay.getWidth()+ " " +mDisplay.getHeight()), Toast.LENGTH_SHORT).show();
    }

    public void startGame() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        //mTimerRunnable.run();
    }

    public void stopGame() {

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                mSensorX = event.values[0];
                mSensorY = event.values[1];
                break;
            case Surface.ROTATION_90:
                mSensorX = -event.values[1];
                mSensorY = event.values[0];
                break;
            case Surface.ROTATION_180:
                mSensorX = -event.values[0];
                mSensorY = -event.values[1];
                break;
            case Surface.ROTATION_270:
                mSensorX = event.values[1];
                mSensorY = -event.values[0];
                break;
        }
        mSensorZ = event.values[2];
        mSensorTimeStamp = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mXOrigin = w * 0.5f;
        mYOrigin = h * 0.5f;

        mHorizontalBound = (w - mPlayer.getLine().mSize) * 0.5f;
        mVerticalBound = (h - mPlayer.getLine().mSize) * 0.5f;
    }

    public void BresenhamLine(int x1, int y1, int x2, int y2, Paint paint, Bitmap bitmap, int width)
    {
        int d, dx, dy, ai, bi, xi, yi;
        int x = x1, y = y1;
        // kierunek rysowania
        if (x1 < x2)
        {
            xi = 1;
            dx = x2 - x1;
        }
        else
        {
            xi = -1;
            dx = x1 - x2;
        }
        // kierunek rysowania
        if (y1 < y2)
        {
            yi = 1;
            dy = y2 - y1;
        }
        else
        {
            yi = -1;
            dy = y1 - y2;
        }
        // pierwszy pixel
        //for(int i=x;i<x+width;++i)
        //{
        bitmap.setPixel(x,y,Color.GREEN);
        //}
        // wiodąca oś OX
        if (dx > dy)
        {
            ai = (dy - dx) * 2;
            bi = dy * 2;
            d = bi - dx;
            while (x != x2)
            {
                if (d >= 0)
                {
                    x += xi;
                    y += yi;
                    d += ai;
                }
                else
                {
                    d += bi;
                    x += xi;
                }
                //for(int i=x;i<x+width;++i)
                //{
                bitmap.setPixel(x,y,Color.GREEN);
                //}
            }
        }
        // wiodąca oś OY
        else
        {
            ai = (dx - dy) * 2;
            bi = dx * 2;
            d = bi - dy;
            while (y != y2)
            {
                if (d >= 0)
                {
                    x += xi;
                    y += yi;
                    d += ai;
                }
                else
                {
                    d += bi;
                    y += yi;
                }
                //for(int i=x;i<x+width;++i)
                //{
                bitmap.setPixel(x,y,Color.GREEN);
                //}
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBitmapPoint = getDrawingCache();
        canvas.drawBitmap(mBitmapPoint,0,0,null);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setTextSize(25);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);

        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(mPlayer.getLine().mSize);
        paint2.setColor(Color.GREEN);

        canvas.drawText(String.valueOf(mSensorY),15,15,paint);
        //if (mBitmapPoint != null)
        //{

        float x = mPlayer.getLine().mX;
        float y = mPlayer.getLine().mY;
        if(mLogic.movePlayer(mBitmapPoint, mPlayer, mSensorY)==false)
        {
            //Toast.makeText((Activity) getParent(), "You lose.", Toast.LENGTH_SHORT).show();
        }
        //mBitmapPoint.setPixels();

        //

        BresenhamLine((int)x,(int)y,(int)mPlayer.getLine().mX, (int)mPlayer.getLine().mY, null, mBitmapPoint,10);
        //canvas.drawLine(x,y,mPlayer.getLine().getmPosX(), mPlayer.getLine().getmPosY(), paint2);

        canvas.drawBitmap(mBitmapPoint, 0, 0, null);
        invalidate();


        //

        //}


    }

}
