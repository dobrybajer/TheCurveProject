package com.student.thecurvegame.Models;

import android.graphics.Point;

public class ExtPoint extends Point {
    public float oldX;
    public float oldY;
    public float newX;
    public float newY;

    public ExtPoint(float oX, float oY,float nX,float nY)
    {
        oldX = oX;
        oldY = oY;
        newX = nX;
        newY = nY;
    }
}