package com.example.thecurve;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import java.util.ArrayList;

public class GameActivity extends Activity {

    private PowerManager.WakeLock mWakeLock;
    private GameSurfaceView mGameSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        ArrayList<String> gamers= new ArrayList<String>();
        ArrayList<Integer> mWspStart=new ArrayList<Integer>();

        if(intent.getStringArrayListExtra("test") != null)
        {
             gamers=intent.getStringArrayListExtra("test");
        }else {
            gamers.add("mis");
        }

        if(intent.getStringArrayListExtra("startwsp") != null)
        {
            mWspStart=intent.getIntegerArrayListExtra("startwsp");
        }else {
            mWspStart.add(150);
            mWspStart.add(150);
        }

        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        mGameSurfaceView = new GameSurfaceView(this);
        mGameSurfaceView.setPlayerList(gamers,mWspStart);
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