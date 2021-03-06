package com.student.thecurvegame.Models;

import android.app.Activity;
import android.content.Context;

import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;
import com.student.thecurvegame.Activities.LogActivity;
import com.student.thecurvegame.Views.GameSurfaceView;

import java.util.ArrayList;
import java.util.List;

public class Chord extends Activity {

    public ChordManager mChordManager = null;

    private Context context = null;

    private static final String CHORD_HELLO_TEST_CHANNEL = "com.samsung.android.sdk.chord.example.HELLOTESTCHANNEL";

    private static final String CHORD_SAMPLE_MESSAGE_TYPE = "com.samsung.android.sdk.chord.example.MESSAGE_TYPE";

    public int mCount = 1;
    public IChordChannel channel = null;
    public String node = null;

    public ArrayList<String> nodes = new ArrayList<String>();
    private LogActivity mlogact = null;
    private GameSurfaceView gameSurfaceView = null;

    public Chord(Context mcontect, LogActivity _logact) {
        mlogact = _logact;
        context = mcontect;
        initChord();
        startChord();
    }


    private void startChord() {
        List<Integer> infList = mChordManager.getAvailableInterfaceTypes();
        if (infList.isEmpty()) {
            SetText("No Interace");
        }
        int interfacetype = 0;

        if (infList.isEmpty() == false) {
            interfacetype = infList.get(0);
            int nError = mChordManager.start(interfacetype, mManagerListener);
            if (nError != ChordManager.ERROR_NONE) {
                SetText("Chord Error");
                mChordManager.close();
            }
        } else if (infList.isEmpty() == true) {
            mlogact.SetMessage();
        }
    }

    IChordManagerListener mManagerListener = new IChordManagerListener() {
        @Override
        public void onStarted(String nodeName, int reason) {
            if (reason == STARTED_BY_USER) {
                SetText("Connected to WI-FI");
            }
            joinTestChannel();
        }

        @Override
        public void onNetworkDisconnected() {
            mChordManager.stop();
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
        IChordChannel channel = null;
        channel = mChordManager.joinChannel(CHORD_HELLO_TEST_CHANNEL, mChannelListener);
        if (channel == null) {
            SetText("No Channel");
        }
    }

    public IChordChannelListener mChannelListener = new IChordChannelListener() {
        @Override
        public void onNodeJoined(String fromNode, String fromChannel) {
            byte[][] payload = new byte[1][];
            node = fromNode;
            nodes.add(node);
            payload[0] = mlogact.GetPlayer().toString().getBytes();
            channel = mChordManager.getJoinedChannel(fromChannel);
            channel.sendData(fromNode, CHORD_SAMPLE_MESSAGE_TYPE, payload);
        }

        @Override
        public void onNodeLeft(String s, String s2) {
        }

        @Override
        public void onDataReceived(String fromNode, String fromChannel, String payloadType,
                                   byte[][] payload) {
            if (payloadType.equals(CHORD_SAMPLE_MESSAGE_TYPE)) {
                String name = new String(payload[0]);
                char[] tab = name.toCharArray();
                char[] tmp = new char[tab.length - 1];
                for (int i = 0; i < tab.length - 1; i++) {
                    tmp[i] = tab[i];
                }
                mlogact.AddPlayers(new String(tmp), 0);
                mCount++;
                SetText("Ready to Play !");
            }
            if (payloadType.equals("START")) {
                StartGame();
            }

            if (payloadType.equals("MOVE")) {
                ExtPoint mExtPoint = new ExtPoint(new Integer(new String(payload[0])), new Integer(new String(payload[1])),
                        new Integer(new String(payload[2])), new Integer(new String(payload[3])));
                Integer color = new Integer(new String(payload[4]));
                gameSurfaceView.DrawLocalPlayers(mExtPoint, color);
            }

            if (payloadType.equals("DEAD")) {
                gameSurfaceView.PlayerDead();
            }

            if (payloadType.equals("STOP")) {
                gameSurfaceView.stopGame();
            }

            if (payloadType.equals("REPLAY")) {
                gameSurfaceView.startGame();
            }
            if (payloadType.equals("DECREASE_CNT")) {
                mlogact.DecreaseCounter();
            }
            if (payloadType.equals("RESULT")) {
                mlogact.ShowResult();
            }

            if (payloadType.equals("INCREASE")) {
                String name = new String(payload[0]);
                char[] tab = name.toCharArray();
                char[] tmp = new char[tab.length - 1];
                for (int i = 0; i < tab.length - 1; i++) {
                    tmp[i] = tab[i];
                }
                mlogact.Increase(new String(tmp));
            }

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
        if (mChordManager == null) {

            mChordManager = ChordManager.getInstance(context);
            mChordManager.setHandleEventLooper(context.getMainLooper());
        }
    }

    public LogActivity GetLog() {
        return mlogact;
    }

    public int getCount() {
        return mCount;
    }

    public void StartGame() {
        mlogact.Start(mCount);
    }

    public void SetText(String text) {
        if (mlogact != null)
            mlogact.SetText(text);
    }

    public void StopChord() {
        mChordManager.close();
    }

    public ArrayList<String> getNode() {
        return nodes;
    }

    public void setGameSurfaceView(GameSurfaceView v) {
        gameSurfaceView = v;
    }

}
