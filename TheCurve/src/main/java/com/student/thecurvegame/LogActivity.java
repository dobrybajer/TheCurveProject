package com.student.thecurvegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;


import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.os.Parcelable;
import android.os.PowerManager;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;

import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

import com.student.thecurvegame.Activities.GameActivity;
import com.student.thecurvegame.Models.Player;
import com.student.thecurvegame.Views.GameSurfaceView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class LogActivity extends Activity {

    Button btn_game;
    Button btn_chord;

    private LogActivity logact=null;
    private Chord mChord=null;

    private String PlayerName="Player";

    private int mPlayerCount=0;
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

    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_game:
                    if(mChord!=null )
                    {

                        mPlayerCount=mChord.getCount();
                        try {
                        mChord.channel.sendData(mChord.node, "START", null);
                        }
                        catch(Exception e)
                        {

                        }
                    }


                    Start(mPlayerCount);

                    break;
                case R.id.button_chord:

                     mChord=new Chord(getApplicationContext(),LogActivity.this);
                    break;

            }
        }
    };

    public void Start(int count)
    {


            mGameSurfaceView = new GameSurfaceView(this);
            //mGameSurfaceView.setPlayerList(_names,_Wsp);
           // mGameSurfaceView.setCount(count);
            if(mChord !=null)
            mChord.setGameSurfaceView(mGameSurfaceView);
            //mGameSurfaceView.startGame();
            mGameSurfaceView.setChord(mChord);
            mGameSurfaceView.setCount(count);
            mGameSurfaceView.setPlayer(PlayerName);
            mGameSurfaceView.startGame();
            setContentView(mGameSurfaceView);

    }


    public void SetText(String Text)
    {
        Button tex =(Button)findViewById(R.id.button_chord);
        tex.setText(Text);
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        mGameSurfaceView.startGame();
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        if(mChord!=null)
        mChord.mChordManager.stop();
        /*mGameSurfaceView.stopGame();
        mWakeLock.release();*/
    }

    public void SetMessage()
    {
        AlertDialog alertDialog= new AlertDialog.Builder(this).create();
        alertDialog.setTitle("NO WI-FI CHANNEL");
        alertDialog.setMessage("turn on wi-i mode ");
        alertDialog.show();
    }

    public void GetName()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LogActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Name");

        // Setting Dialog Message
        alertDialog.setMessage("Enter Name");
        final EditText input = new EditText(LogActivity.this);
        input.setText("Player");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);


        // Setting Icon to Dialog


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog

                        PlayerName = input.getText().toString();
                    }
                    });


        alertDialog.show();

    }

}


