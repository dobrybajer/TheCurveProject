package com.student.thecurvegame.Views;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.student.thecurvegame.Chord;
import com.student.thecurvegame.Controllers.GameAsyncTask;
import com.student.thecurvegame.LogActivity;
import com.student.thecurvegame.Models.ExtPoint;
import com.student.thecurvegame.Models.Logic;
import com.student.thecurvegame.Models.Player;

import java.util.ArrayList;
import java.util.List;

public class GameSurfaceView extends SurfaceView implements SensorEventListener {

    private static final int TURN_RIGHT = -1;
    private static final int TURN_LEFT = 1;
    private static final int GO_STRAIGHT = 0;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    boolean isSensorActive;

    private Display mDisplay;
    private int mWidth;
    private int mHeight;

    private Bitmap mBitmapPoint;
    private Canvas mainCanvas;
    private Paint linePaint;

    private GameAsyncTask gameAsyncTask;
    private SurfaceHolder surfaceHolder;
    private boolean running;

    private Player mPlayer;
    private Logic mLogic;
    private float mTurnVar;
    private float mCalibratedSensor;
    public List<Player> mPlayerList=new ArrayList<Player>();
    private boolean mStart;

    private Chord mChord=null;

    public GameSurfaceView(Context context) {
        super(context);

        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        Point size = new Point();
        mDisplay.getSize(size);
        mWidth = size.x;
        mHeight = size.y;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        isSensorActive = sp.getBoolean("prefSensorControl", false);

        if(isSensorActive) {
            mCalibratedSensor = sp.getFloat("prefSensor", 0);
            mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        surfaceHolder = getHolder();
        mBitmapPoint = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mainCanvas = new Canvas(mBitmapPoint);
        mainCanvas.drawColor(Color.TRANSPARENT);

        mStart=true;
    }

    public float getLineX()
    {
        return mPlayerList.get(0).getLine().mX;
    }

    public float getLineY()
    {
        return mPlayerList.get(0).getLine().mY;
    }

    public boolean Compute() {

            mPlayer=mPlayerList.get(0);
            return mLogic.movePlayer(mBitmapPoint, mPlayer, mTurnVar);

    }

    public void DrawLocal(ExtPoint p) {

            mPlayer=mPlayerList.get(0);
            mainCanvas.drawLine(p.oldX,  p.oldY,  p.newX,  p.newY, linePaint);
            if(mPlayerList.size()!=1)
            SendPoint(p);

    }

    public void Draw() {
        for(int index=0;index<1;index++)
        {
            mPlayer=mPlayerList.get(index);
            Canvas c=null;
            if (!mPlayer.isDead() && running) {
                if(surfaceHolder.getSurface().isValid()){
                    try {
                        c = surfaceHolder.lockCanvas();
                        synchronized (surfaceHolder)
                        {
                            c.drawBitmap(mBitmapPoint,0,0,null);
                        }
                    }
                    finally {
                        if(c!=null) {
                            surfaceHolder.unlockCanvasAndPost(c);
                        }
                    }
                }
            }
        }
    }

    public void startGame() {
        if(isSensorActive) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        running = true;
        gameAsyncTask = new GameAsyncTask(this);
        gameAsyncTask.execute();
    }

    public void stopGame() {
        if(isSensorActive) {
            mSensorManager.unregisterListener(this);
        }
        running = false;
        if(gameAsyncTask.getStatus()!= AsyncTask.Status.FINISHED)
        {
            gameAsyncTask.cancel(true);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
        if(isSensorActive)
            mTurnVar = event.values[1];//+mPrefVar;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isSensorActive)
        {
            float x = event.getX();
            int eventaction = event.getAction();
            switch (eventaction) {
                case MotionEvent.ACTION_DOWN:
                    if (x > mWidth / 2) { mTurnVar = TURN_RIGHT; }
                    if (x < mWidth / 2) { mTurnVar = TURN_LEFT; }
                    return true;
                case MotionEvent.ACTION_UP:
                    mTurnVar = GO_STRAIGHT;
                    return true;
            }
            return super.onTouchEvent(event);
        }
        return false;
    }

    public void setPlayerList(ArrayList<String> newlist,ArrayList<Integer> newwsp)
    {
        if(newlist.isEmpty()==false)
        {
            for(String gam : newlist)
            {
                mPlayerList.add(new Player(Color.BLUE,gam, getContext()));

            }
        }else if(newlist.isEmpty()==true)
        {
            mPlayerList.add(new Player(Color.BLUE,"misp", getContext()));
        }
        int indexx=0;

        if(newwsp.isEmpty()==false)
        {
            for(Player pla : mPlayerList)
            {
                mLogic = new Logic((Activity) this.getContext());
                mLogic.setPlayersOnStart(pla);

                pla.getLine().mX=(float)newwsp.get(indexx);
                pla.getLine().mY=(float)newwsp.get(indexx+1);
                pla.getLine().mVel=(float)newwsp.get(indexx+2);
                pla.getLine().mAngle=(float)newwsp.get(indexx+3);
                indexx+=4;
                linePaint = new Paint();
                linePaint.setAntiAlias(true);
                linePaint.setStrokeWidth(pla.getLine().mSize);
                linePaint.setColor(pla.getColor());
            }
        }else if( newwsp.isEmpty()==true)
        {
            mLogic = new Logic((Activity) this.getContext());
            mLogic.setPlayersOnStart(mPlayerList.get(0));
            linePaint = new Paint();
            linePaint.setAntiAlias(true);
            linePaint.setStrokeWidth(mPlayerList.get(0).getLine().mSize);
            linePaint.setColor(mPlayerList.get(0).getColor());
        }



    }

    public void SendPoint(ExtPoint p)
    {
        byte[][] payload = new byte[4][];

        Integer[] point=new Integer[]{(int)p.oldX,(int)p.oldY,(int)p.newX,(int)p.newY};

        payload[0]=point[0].toString().getBytes();
        payload[1]=point[1].toString().getBytes();
        payload[2]=point[2].toString().getBytes();
        payload[3]=point[3].toString().getBytes();

        mChord.channel.sendData(mChord.node, "MOVE", payload);
    }

    public void setChord(Chord _Chord)
    {
        mChord=_Chord;
    }
}
