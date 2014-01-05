package com.example.thecurve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button btn_start;
    Button btn_help;
    Button btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button) findViewById(R.id.button_start);
        btn_help = (Button) findViewById(R.id.button_help);
        btn_close = (Button) findViewById(R.id.button_close);

        btn_start.setOnClickListener(onClickListener);
        btn_close.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_start:
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_close:
                    finish();
                    System.exit(0);
                    break;
            }
        }
    };
}

