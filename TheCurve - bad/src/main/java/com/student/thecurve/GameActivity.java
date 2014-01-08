package com.student.thecurve;


import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;

public class GameActivity extends Activity {

    private PowerManager.WakeLock mWakeLock;
    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        mGameView = new GameView(this);
        setContentView(mGameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        mGameView.startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGameView.stopGame();
        mWakeLock.release();
    }
}
