package com.student.thecurvegame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import com.samsung.chord.ChordManager;

import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;
import com.student.thecurvegame.Models.ExtPoint;
import com.student.thecurvegame.Models.Logic;
import com.student.thecurvegame.Models.Player;
import com.student.thecurvegame.Views.GameSurfaceView;

import java.util.ArrayList;
import java.util.List;

public class Chord extends Activity{

    private ChordManager mChordManager =null;
    private String PlayerName;
    private ArrayList<String> names=new ArrayList<String>();
    private ArrayList<Integer> mWspStart=new ArrayList<Integer>();

    private Context context=null;

    private static final String CHORD_HELLO_TEST_CHANNEL = "com.samsung.android.sdk.chord.example.HELLOTESTCHANNEL";

    private static final String CHORD_SAMPLE_MESSAGE_TYPE = "com.samsung.android.sdk.chord.example.MESSAGE_TYPE";

    public IChordChannel channel=null;
    public String node=null;

    private LogActivity mlogact=null;
    private GameSurfaceView gameSurfaceView=null;

    public Chord(Context mcontect,String _PlayerName,LogActivity _logact)
    {
        mlogact=_logact;
        PlayerName=_PlayerName;
        context=mcontect;
        initChord();
        startChord();
    }



    private void startChord() {

        List<Integer> infList=mChordManager.getAvailableInterfaceTypes();
        if(infList.isEmpty())
        {
                SetText("No Interace");

        }

        int interfacetype=0;

        if(infList.isEmpty()==false)
        {
            interfacetype=infList.get(0);
            int nError=mChordManager.start(interfacetype,mManagerListener);
            if( nError != ChordManager.ERROR_NONE)
            {
                SetText("Chord Error");
                mChordManager.close();
            }


        }else if(infList.isEmpty()==true)
        {
            mlogact.SetMessage();

        }

        /*int nError=mChordManager.start(interfacetype,mManagerListener);
        if( nError != ChordManager.ERROR_NONE)
        {
            SetText("Chord Error");
            mChordManager.close();
        }*/

    }

    IChordManagerListener mManagerListener = new IChordManagerListener() {
        @Override
        public void onStarted(String nodeName, int reason) {
            if(reason == STARTED_BY_USER)
            {
                SetText("Player");
            }

            joinTestChannel();

        }

        @Override
        public void onNetworkDisconnected() {

        }

        @Override
        public void onError(int i) {
            mChordManager.stop();
        }

        @Override
        public void onStopped(int i) {
            mChordManager.stop();
        }
    };

    public void joinTestChannel() {
        IChordChannel channel =null;

            channel = mChordManager.joinChannel(CHORD_HELLO_TEST_CHANNEL,mChannelListener);

        if(channel == null)
        {
            SetText("NO Channel");
         }
    }

    public IChordChannelListener mChannelListener= new IChordChannelListener() {
        @Override
        public void onNodeJoined(String fromNode, String fromChannel) {
            byte[][] payload = new byte[6][];

            payload[0]=PlayerName.getBytes();


            names.add(PlayerName);
            Player player1=new Player(Color.BLUE,PlayerName, Chord.this.context);
            Logic logic = new Logic(Chord.this);
            logic.setPlayersOnStart(player1);


            mWspStart.add(new Integer((int)player1.getLine().mX));
            mWspStart.add(new Integer((int)player1.getLine().mY));
            mWspStart.add(new Integer((int)player1.getLine().mVel));
            mWspStart.add(new Integer((int)player1.getLine().mAngle));

            payload[1]=mWspStart.get(0).toString().getBytes();
            payload[2]=mWspStart.get(1).toString().getBytes();
            payload[3]=mWspStart.get(2).toString().getBytes();
            payload[4]=mWspStart.get(3).toString().getBytes();


            node=fromNode;
            channel = mChordManager.getJoinedChannel(fromChannel);
            channel.sendData(fromNode, CHORD_SAMPLE_MESSAGE_TYPE, payload);
            SetText("Send Data");
        }

        @Override
        public void onNodeLeft(String s, String s2) {
            SetText("Left Player");
        }

        @Override
        public void onDataReceived(String fromNode, String fromChannel, String payloadType,
                                   byte[][] payload) {
            if(payloadType.equals(CHORD_SAMPLE_MESSAGE_TYPE)){


            String name = new String(payload[0]);
            names.add(name);

            mWspStart.add(new Integer(new String(payload[1])));
            mWspStart.add(new Integer(new String(payload[2])));
            mWspStart.add(new Integer(new String(payload[3])));
            mWspStart.add(new Integer(new String(payload[4])));

            }
            if(payloadType.equals("START"))
            {
                StartGame();
            }

            if(payloadType.equals("MOVE"))
            {
                ExtPoint mExtPoint= new ExtPoint(new Integer(new String(payload[0])),new Integer(new String(payload[1])),
                        new Integer(new String(payload[2])),new Integer(new String(payload[3])));
                gameSurfaceView.DrawLocal(mExtPoint);
            }

            SetText("Recived Data");
        }

        @Override
        public void onFileWillReceive(String s, String s2, String s3, String s4, String s5, String s6, long l) {

        }

        @Override
        public void onFileChunkReceived(String s, String s2, String s3, String s4, String s5, String s6, long l, long l2) {

        }

        @Override
        public void onFileReceived(String s, String s2, String s3, String s4, String s5, String s6, long l, String s7) {

        }

        @Override
        public void onFileChunkSent(String s, String s2, String s3, String s4, String s5, String s6, long l, long l2, long l3) {

        }

        @Override
        public void onFileSent(String s, String s2, String s3, String s4, String s5, String s6) {

        }

        @Override
        public void onFileFailed(String s, String s2, String s3, String s4, String s5, int i) {

        }

        @Override
        public void onMultiFilesWillReceive(String s, String s2, String s3, String s4, int i, String s5, long l) {

        }

        @Override
        public void onMultiFilesChunkReceived(String s, String s2, String s3, String s4, int i, String s5, long l, long l2) {

        }

        @Override
        public void onMultiFilesReceived(String s, String s2, String s3, String s4, int i, String s5, long l, String s6) {

        }

        @Override
        public void onMultiFilesChunkSent(String s, String s2, String s3, String s4, int i, String s5, long l, long l2, long l3) {

        }

        @Override
        public void onMultiFilesSent(String s, String s2, String s3, String s4, int i, String s5) {

        }

        @Override
        public void onMultiFilesFailed(String s, String s2, String s3, String s4, int i, int i2) {

        }

        @Override
        public void onMultiFilesFinished(String s, String s2, String s3, int i) {

        }
    };



    private void initChord() {
        if(mChordManager==null)
        {

            mChordManager = ChordManager.getInstance(context);
            mChordManager.setHandleEventLooper(context.getMainLooper());

        }
    }
    public ArrayList<String> getArrayName()
    {
        return names;
    }

    public ArrayList<Integer> getArrayWsp()
    {
        return mWspStart;
    }


    public void StartGame()
    {
        mlogact.Start(names, mWspStart);

    }

    public void SetText(String text)
    {
        if(mlogact!=null)
        mlogact.SetText(text);
    }

    public void StopChord()
    {
        mChordManager.close();
    }

    public void setGameSurfaceView(GameSurfaceView v)
    {
        gameSurfaceView=v;
    }

}
