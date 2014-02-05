package com.student.thecurvegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;


import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.os.Parcelable;
import android.os.PowerManager;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



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

    private String PlayerName;
    private ArrayList<String> names=new ArrayList<String>();
    private ArrayList<Integer> mWspStart=new ArrayList<Integer>();

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

        TextView tex =(TextView)findViewById(R.id.text);
        tex.setText("@string/connect");

        mGameSurfaceView = new GameSurfaceView(this);




    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_game:
                    if(mChord!=null )
                    {
                        names=mChord.getArrayName();
                        mWspStart=mChord.getArrayWsp();
                        try {
                        mChord.channel.sendData(mChord.node, "START", null);
                        }
                        catch(Exception e)
                        {
                            //Start(names,mWspStart);
                        }
                    }


                    Start(names,mWspStart);

                    break;
                case R.id.button_chord:

                     EditText text =(EditText)findViewById(R.id.player_name);
                     PlayerName = text.getText().toString();
                     mChord=new Chord(getApplicationContext(),PlayerName,LogActivity.this);


                    break;

            }
        }
    };

    public void Start(ArrayList<String> _names,ArrayList<Integer> _Wsp)
    {


            mGameSurfaceView = new GameSurfaceView(this);
            mGameSurfaceView.setPlayerList(names,_Wsp);
            if(mChord !=null)
            mChord.setGameSurfaceView(mGameSurfaceView);
            mGameSurfaceView.startGame();
            mGameSurfaceView.setChord(mChord);

            setContentView(mGameSurfaceView);

    }

    public void SetText(String Text)
    {
      /*  TextView tex =(TextView)findViewById(R.id.text);
        tex.setText(Text);*/
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        mWakeLock.acquire();
        mGameSurfaceView.startGame();
    }*/

  /*  @Override
    protected void onPause() {
        super.onPause();
        mGameSurfaceView.stopGame();
        mWakeLock.release();
    }*/

    public void SetMessage()
    {
        AlertDialog alertDialog= new AlertDialog.Builder(this).create();
        alertDialog.setTitle("NO WI-FI CHANNEL");
        alertDialog.setMessage("turn on wi-i mode ");
        alertDialog.show();
    }
}


