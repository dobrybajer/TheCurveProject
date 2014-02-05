package com.student.thecurvegame.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.student.thecurvegame.LogActivity;
import com.student.thecurvegame.R;


public class MainActivity extends Activity {

    Button btn_start;
    Button btn_close;
    Button btn_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        btn_start = (Button)findViewById(R.id.button_start);
        btn_setting = (Button)findViewById(R.id.button_settings);
        btn_close = (Button)findViewById(R.id.button_close);

        btn_start.setOnClickListener(onClickListener);
        btn_close.setOnClickListener(onClickListener);
        btn_setting.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.button_start:
                    Intent intent = new Intent(getApplicationContext(),  LogActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_settings:
                    Intent intent2 = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.button_close:
                    finish();
                    System.exit(0);
                    break;
            }
        }
    };
}
