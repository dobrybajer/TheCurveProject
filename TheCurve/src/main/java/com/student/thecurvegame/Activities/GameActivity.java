package com.student.thecurvegame.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

import com.student.thecurvegame.Message;
import com.student.thecurvegame.Views.GameSurfaceView;

public class GameActivity extends Activity {

    private PowerManager.WakeLock mWakeLock;
    private GameSurfaceView mGameSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();

        Message message=null;
        if(intent.getParcelableExtra("Message") != null)
        {
            message=intent.getParcelableExtra("Message");
        }


        mGameSurfaceView = new GameSurfaceView(this);
        //mGameSurfaceView.setPlayerList(message.getNames(),message.getWsp());

        setContentView(mGameSurfaceView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        mGameSurfaceView.startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGameSurfaceView.stopGame();
        mWakeLock.release();
    }
}
