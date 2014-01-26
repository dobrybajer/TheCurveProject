package com.student.thecurve;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;


public class Logic {

    private Activity mainActivity;

    public Logic(final Activity activity)
    {
        this.mainActivity = activity;
    }
    public void onCollideLine(Player player) {
        player.setIsDead(true);
        this.onPlayerIsDead(player);
    }

    public void onCollideWall(Player player) {
        player.setIsDead(true);
        this.onPlayerIsDead(player);
    }

    public void onPlayerIsDead(Player player) {
        Toast.makeText(mainActivity, "Fuck off", Toast.LENGTH_SHORT).show();
        if (isRoundEnded())
            onRoundEnded();
    }

    public void playerTurnLeft(Player player) {
        player.getLine().turnLeft();
    }

    public void playerTurnRight(Player player) {
        player.getLine().turnRight();
    }

    /*public void prepareStep() {
        if (Math.random() <= 1.0 / 180) {
            Extra e = Extra.getRandomExtra();
            e.x = this.getRandomInX(20);
            e.y = this.getRandomInY(20);
            Spiel.CURRENT_SPIEL.extras.add(e);
        }
    }*/

    /*public int getRandomInX(int d) {
        return (int) (Math.random() * (Player.GAME_WIDTH - 2 * d) + d);
    }

    public int getRandomInY(int d) {
        return (int) (Math.random() * (Spiel.GAME_HEIGHT - 2 * d) + d);
    }*/

    public boolean movePlayer(Bitmap bitmap, Player player, float sensorY) {
        if (player.isDead())
            return false;

        Line l = player.getLine();

        if (sensorY<-2)
            l.turnLeft();
        if (sensorY>2)
            l.turnRight();

        float ex = (float) (Math.cos(l.mAngle) * l.mVel);
        float ey = (float) (Math.sin(l.mAngle) * l.mVel);
        float x = l.mX + ex;
        float y = l.mY + ey;

        if (x < 0 || x >= bitmap.getWidth() || y < 0 || y >= bitmap.getHeight()) {
            this.onCollideWall(player);
            return false;
        }

        if(bitmap.getPixel((int)x,(int)y)==Color.GREEN)
        {
            this.onCollideLine(player);
            return false;
        }

        l.moveStep();

        return true;
    }

    public boolean isRoundEnded() {
        int notDeadPlayers = 1;

        /*Player[] player = Spiel.CURRENT_SPIEL.getSpieler();
        for (Spieler s : spieler) {
            if (!s.isTot())
                anzahlLebende += 1;
        }*/
        return notDeadPlayers <= 1;
    }

    private void onRoundEnded() {
        //Spiel.CURRENT_SPIEL.stopRunde();
        //Spiel.CURRENT_SPIEL.setAwaitRunde(true);
    }

    public void setPlayersOnStart(Player player) {
        Line l = player.getLine();
        l.mX = (float) (Math.random() * (300 - 100) + 50);
        l.mY = (float) (Math.random() * (300 - 100) + 50);
        l.mVel = 5;
        l.mAngle = (float) (Math.random() * 360);
        l.mSize = 8;
        player.setIsDead(false);
    }
}