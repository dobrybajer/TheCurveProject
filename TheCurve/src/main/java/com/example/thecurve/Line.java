package com.example.thecurve;

public class Line {
    public float mSize;
    public float mAngle;
    public float mVel;
    public float mX;
    public float mY;

    public Line() {
    }

    public void moveStep() {
        mX += (float) Math.cos(mAngle) * mVel;
        mY += (float) Math.sin(mAngle) * mVel;
    }

    public void turnLeft() {
        mAngle += (float) Math.PI / 20;
    }

    public void turnRight() {
        mAngle -= (float) Math.PI / 20;
    }
}