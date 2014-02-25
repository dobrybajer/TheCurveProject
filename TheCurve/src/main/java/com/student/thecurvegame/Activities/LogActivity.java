package com.student.thecurvegame.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.student.thecurvegame.Models.Chord;
import com.student.thecurvegame.R;
import com.student.thecurvegame.Views.GameSurfaceView;

import java.util.ArrayList;


public class LogActivity extends Activity {

    Button btn_game;
    Button btn_chord;

    private LogActivity logact = null;
    private Chord mChord = null;

    private String PlayerName = "Player";
    private ArrayList<com.student.thecurvegame.Models.Dictionary> PlayerPoint = null;
    private int mPlayerCount = 1;
    private int mPlayerCountGlobal = 1;
    private PowerManager.WakeLock mWakeLock;
    private GameSurfaceView mGameSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_log);

        btn_game = (Button) findViewById(R.id.button_game);
        btn_chord = (Button) findViewById(R.id.button_chord);
        btn_game.setOnClickListener(onClickListener);
        btn_chord.setOnClickListener(onClickListener);
        GetName();
        PlayerPoint = new ArrayList<com.student.thecurvegame.Models.Dictionary>();


    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_game:
                    if (mChord != null) {

                        mPlayerCount = mChord.getCount();
                        mPlayerCountGlobal = mChord.getCount();
                        try {
                            for (String n : mChord.nodes) {
                                mChord.channel.sendData(n, "START", null);
                            }
                        } catch (Exception e) {

                        }
                    }


                    Start(mPlayerCount);

                    break;
                case R.id.button_chord:

                    mChord = new Chord(getApplicationContext(), LogActivity.this);
                    break;

            }
        }
    };

    @Override
    public void onBackPressed() {
        if (mPlayerCount > 1) {
            mGameSurfaceView.stopGame();
            for (String n : mChord.nodes) {
                mChord.channel.sendData(n, "STOP", null);
            }
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Stop Game")
                    .setMessage("Are you sure you want to finish the game?")
                    .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Start(mPlayerCountGlobal);
                            for (String n : mChord.nodes) {
                                mChord.channel.sendData(n, "START", null);
                            }
                        }
                    })
                    .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mGameSurfaceView.startGame();
                            for (String n : mChord.nodes) {
                                mChord.channel.sendData(n, "REPLAY", null);
                            }
                            dialog.cancel();
                        }
                    }).setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for (String n : mChord.nodes) {
                        mChord.channel.sendData(n, "DECREASE_CNT", null);
                    }

                    finish();
                }
            }).show();
        } else {
            super.onBackPressed();
        }
    }

    public void DecreaseCounter() {
        mPlayerCountGlobal--;
    }

    public void Start(int count) {
        if (mChord != null) {
            mPlayerCount = mChord.getCount();
            mPlayerCountGlobal = mChord.getCount();
        }
        mGameSurfaceView = new GameSurfaceView(this);

        if (mChord != null) {
            mChord.setGameSurfaceView(mGameSurfaceView);
            mGameSurfaceView.setChord(mChord);
        }
        mGameSurfaceView.setCount(count);
        mGameSurfaceView.setPlayer(PlayerName);
        mGameSurfaceView.startGame();
        setContentView(mGameSurfaceView);

    }

    public void AddPlayers(String name, int val) {
        PlayerPoint.add(new com.student.thecurvegame.Models.Dictionary(name, val));
    }

    public ArrayList<com.student.thecurvegame.Models.Dictionary> GetArrayPoints() {
        return PlayerPoint;
    }

    public void SetText(String Text) {
        Button tex = (Button) findViewById(R.id.button_chord);
        tex.setText(Text);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mChord != null)
            mChord.mChordManager.stop();

    }

    public void SetMessage() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("NO WI-FI CHANNEL");
        alertDialog.setMessage("turn on wi-i mode ");
        alertDialog.show();
    }

    public String GetPlayer() {
        return PlayerName;
    }


    public void GetName() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LogActivity.this);


        alertDialog.setTitle("Name");


        alertDialog.setMessage("Enter Name");
        final EditText input = new EditText(LogActivity.this);
        input.setText("Player");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                        PlayerName = input.getText().toString();
                        PlayerPoint.add(new com.student.thecurvegame.Models.Dictionary(PlayerName, 0));
                    }
                });


        alertDialog.show();

    }

    public void Increase(String name) {
        boolean change = false;
        for (com.student.thecurvegame.Models.Dictionary d : PlayerPoint) {
            if (d.Name.compareTo(name) == 0) {
                change = true;
                d.Points++;
            }
        }

        if (!change) {
            PlayerPoint.get(0).Points++;
        }
    }

    public void ShowResult() {
        String tab = new String();

        for (com.student.thecurvegame.Models.Dictionary d : PlayerPoint) {
            String tmp = new String();
            tmp = d.Name.toString() + " " + d.Points + "\n";
            tab += tmp;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("RESULT");
        alertDialog.setMessage(tab);
        alertDialog.show();
    }
}


