package com.student.thecurvegame.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.student.thecurvegame.R;

public class CalibrationActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor mAccelerometer;

    private int count = 0;
    private long startTime = 0;
    private float mSensor, mCalibVar=0;


    private SharedPreferences sp;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Handler timerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_calibration);

        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        timerHandler = new Handler();
        imageView = (ImageView)findViewById(R.id.imageView);
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;

            if(millis<3100)
            {
                count++;
                mCalibVar=mCalibVar+mSensor;
                Calculate(millis);
            }
            else if(millis==3100)
            {
                imageView.setVisibility(View.VISIBLE);
            }
            else
            {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mCalibVar/=count;

                SharedPreferences.Editor editor = sp.edit();
                editor.putFloat("prefSensor", mCalibVar);
                editor.commit();

                finish();
            }
            timerHandler.postDelayed(this, 100);
        }
    };

    public void Calculate(long promiles)
    {
        Resources res = getResources();
        Rect bounds = progressBar.getProgressDrawable().getBounds();

        if(promiles >= 1500)
        {
            progressBar.setProgressDrawable(res.getDrawable(R.drawable.greenprogressbar));
        }
        else
        {
            progressBar.setProgressDrawable(res.getDrawable(R.drawable.redprogressbar));
        }
        progressBar.getProgressDrawable().setBounds(bounds);
        progressBar.setProgress((int)promiles);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sensorEventListener = this;
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            progressBar.setVisibility(View.VISIBLE);
            mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
        mSensor = event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mSensorManager.unregisterListener(sensorEventListener);
        mSensorManager = null;
        timerHandler.removeCallbacks(timerRunnable);
    }
}
