package com.student.thecurve;

public class Player {
    private int mColor;
    private String mName;
    private boolean mDead;
    private Line mLine;
    private int mPoints;

    public Player(int color, String name)
    {
        this.mColor = color;
        this.mName = name;
        this.mDead = false;
        this.mLine = new Line();
    }

    public void setPoints(int points) {
        this.mPoints = points;
    }

    public int getPoints() {
        return this.mPoints;
    }

    public boolean isDead() {
        return mDead;
    }

    public void setIsDead(boolean isDead) {
        this.mDead = isDead;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public String getName() { return mName; }

    public Line getLine() {
        return mLine;
    }



}
