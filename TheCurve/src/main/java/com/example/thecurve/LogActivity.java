package com.example.thecurve;

import android.app.Activity;
import android.content.Intent;


import android.os.Bundle;

import android.os.Parcelable;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;

import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

import com.example.thecurve.Player;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        btn_game = (Button) findViewById(R.id.button_game);
        btn_chord = (Button) findViewById(R.id.button_chord);
        btn_game.setOnClickListener(onClickListener);
        btn_chord.setOnClickListener(onClickListener);

        TextView tex =(TextView)findViewById(R.id.text);
        tex.setText("@string/connect");



    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_game:
                    if(mChord!=null)
                    {
                        names=mChord.getArrayName();
                        mWspStart=mChord.getArrayWsp();
                        mChord.channel.sendData(mChord.node, "START", null);
                    }


                   /* Intent intent = new Intent(LogActivity.this,GameActivity.class);
                    intent.putStringArrayListExtra("test", names);
                    intent.putIntegerArrayListExtra("startwsp", mWspStart);
                    startActivity(intent);*/
                    Start(names,mWspStart);

                    break;
                case R.id.button_chord:
                   // SetText("Start Chord");
                     EditText text =(EditText)findViewById(R.id.player_name);
                     PlayerName = text.getText().toString();
                     mChord=new Chord(getApplicationContext(),PlayerName,LogActivity.this);

                    break;

            }
        }
    };

    public void Start(ArrayList<String> _names,ArrayList<Integer> _Wsp)
    {
            try {
            Intent intent = new Intent(this,GameActivity.class);
            Message message=new Message(_names,_Wsp);

            intent.putExtra("Message", message);
            startActivity(intent);
            }
            catch(Exception e)
           {
             SetText("Intent Error");

            }
    }

    public void SetText(String Text)
    {
        TextView tex =(TextView)findViewById(R.id.text);
        tex.setText(Text);
    }


}


