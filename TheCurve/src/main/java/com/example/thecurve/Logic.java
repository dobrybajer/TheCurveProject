package com.example.thecurve;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;

import java.util.Random;

public class Logic {

    private Activity mainActivity;

    public Logic(final Activity activity) {
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
        sendMessage();
        if (isRoundEnded())
            onRoundEnded();
    }

    private void sendMessage() {
        new Thread() {
            public void run() {
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(mainActivity, "Fuck off", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }

    public boolean movePlayer(Bitmap bitmap, Player player, float sensorY) {
        if (player.isDead())
            return false;

        Line l = player.getLine();

        if (sensorY < -2)
            l.turnLeft();
        if (sensorY > 2)
            l.turnRight();

        float ex = (float) (Math.cos(l.mAngle) * l.mVel);
        float ey = (float) (Math.sin(l.mAngle) * l.mVel);
        float x = l.mX + ex;
        float y = l.mY + ey;

        if (x <= 0 || x >= bitmap.getWidth() || y <= 0 || y >= bitmap.getHeight()) {
            this.onCollideWall(player);
            return false;
        }

        if (bitmap.getPixel((int) x, (int) y) != Color.TRANSPARENT) {
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

    private int randomColor() {
        int color = new Random().nextInt(7);
        switch (color) {
            case 0:
                return Color.RED;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.GRAY;
            case 5:
                return Color.MAGENTA;
            case 6:
                return Color.CYAN;
            case 7:
                return Color.WHITE;
            default:
                return Color.GREEN;
        }
    }

    public void setPlayersOnStart(Player player) {
        Line l = player.getLine();
        l.mX = (float) (Math.random() * (300 - 100) + 50);
        l.mY = (float) (Math.random() * (300 - 100) + 50);
        l.mVel = 2;
        l.mAngle = (float) (Math.random() * 360);
        l.mSize = 8;
        player.setColor(randomColor());
        player.setIsDead(false);
    }
}