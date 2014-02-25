package com.student.thecurvegame.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Line {

    private int angle;
    public float mSize;
    public float mAngle;
    public float mVel;
    public float mX;
    public float mY;

    public Line(Context con) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(con);
        String s = sp.getString("prefAngle", "20");
        angle = Integer.valueOf(s);
    }

    public void moveStep() {
        mX += (float) Math.cos(mAngle) * mVel;
        mY += (float) Math.sin(mAngle) * mVel;
    }

    public void turnLeft() {
        mAngle += (float) Math.PI / angle;
    }

    public void turnRight() {
        mAngle -= (float) Math.PI / angle;
    }
}