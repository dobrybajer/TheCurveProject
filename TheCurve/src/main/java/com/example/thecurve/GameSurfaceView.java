package com.example.thecurve;

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
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class GameSurfaceView extends SurfaceView implements SensorEventListener, Runnable {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Display mDisplay;

    private Bitmap mBitmapPoint;
    private Canvas mainCanvas;

    private SurfaceHolder surfaceHolder;
    private Thread t;
    private boolean running;

    private int mWidth;
    private int mHeight;

    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;

    public List<Player> mPlayerList=new ArrayList<Player>();
    //public List<Integer> wsp =new ArrayList<Integer>();
    private Player mPlayer;
    //private Player mPlayer2;
    private Logic mLogic;

    private boolean mStart;

    public GameSurfaceView(Context context) {
        super(context);

        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        mWidth = mDisplay.getWidth();
        mHeight = mDisplay.getHeight();

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        mPlayer = new Player(Color.GREEN, "Kamil");
        //mLogic = new Logic((Activity) this.getContext());

           mStart=true;

        mBitmapPoint = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mainCanvas = new Canvas(mBitmapPoint);
        mainCanvas.drawColor(Color.TRANSPARENT);

        surfaceHolder = getHolder();
    }

    public void Compute() {
           for(int index=0;index<mPlayerList.size();index++)
           {
            mPlayer=mPlayerList.get(index);

            //mLogic = new Logic((Activity) this.getContext());
            //mLogic.setPlayersOnStart(mPlayer);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(mPlayer.getLine().mSize);
            paint.setColor(mPlayer.getColor());

            float x = mPlayer.getLine().mX;
            float y = mPlayer.getLine().mY;

            mLogic.movePlayer(mBitmapPoint, mPlayer, mSensorY);
            mainCanvas.drawLine(x, y, mPlayer.getLine().mX, mPlayer.getLine().mY, paint);

            //wysłanie obliczonych punktow
           }

    }

    public void setPlayerList(ArrayList<String> newlist,ArrayList<Integer> newwsp)
    {
        if(newlist.isEmpty()==false)
        {
            for(String gam : newlist)
            {
                mPlayerList.add(new Player(Color.BLUE,gam));

            }
        }else if(newlist.isEmpty()==true)
        {
            mPlayerList.add(new Player(Color.BLUE,"misp"));
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
            }
        }else if( newwsp.isEmpty()==true)
        {
            mLogic = new Logic((Activity) this.getContext());
            mLogic.setPlayersOnStart(mPlayerList.get(0));
        }

    }

    public List<Player> getList(){
        return mPlayerList;
    }
    public boolean getStart(){
        return mStart;
    }

    public Player getPlayers(){
        return mPlayer;
    }

    public void Draw(Canvas c) {
        c.drawBitmap(mBitmapPoint, 0, 0, null);
    }

    public void startGame() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        running = true;
        t = new Thread(this);
        t.start();
    }

    public void stopGame() {
        mSensorManager.unregisterListener(this);
        running = false;
        boolean retry = true;
        running = false;
        while (retry) {
            try {
                t.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        t = null;
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
    public void run() {
        Canvas c = null;
        while (running && !mPlayer.isDead()) {
            if (surfaceHolder.getSurface().isValid()) {
                Compute();
                try {
                    c = surfaceHolder.lockCanvas();
                    t.sleep(10);
                    synchronized (surfaceHolder) {
                        Draw(c);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
}
