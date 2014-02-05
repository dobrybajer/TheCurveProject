package com.student.thecurvegame.Controllers;

import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.student.thecurvegame.Models.ExtPoint;
import com.student.thecurvegame.Views.GameSurfaceView;

public class GameAsyncTask extends AsyncTask<Void, ExtPoint, Void> {

    private GameSurfaceView gameSurfaceView;
    private int speed;

    public GameAsyncTask(GameSurfaceView gsv) {
        gameSurfaceView = gsv;
        String mSpeed = PreferenceManager.getDefaultSharedPreferences(gsv.getContext()).getString("prefSpeed", "10");
        speed = Integer.valueOf(mSpeed);
    }

    @Override
    protected void onProgressUpdate(ExtPoint... progress) {
        super.onProgressUpdate(progress);
        for(ExtPoint p:progress){
            gameSurfaceView.DrawLocal(p);
            gameSurfaceView.Draw();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //android.os.Debug.waitForDebugger();
        float oldX, oldY, newX, newY;
        while(!isCancelled()) {
            oldX = gameSurfaceView.getLineX();
            oldY = gameSurfaceView.getLineY();
            try {
                Thread.sleep(speed);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(gameSurfaceView.Compute()) {
                newX = gameSurfaceView.getLineX();
                newY = gameSurfaceView.getLineY();
                ExtPoint p = new ExtPoint(oldX, oldY, newX, newY);
                publishProgress(p);
            }
            else
                break;
        }
        return null;
    }
}

