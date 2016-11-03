package com.example.android.silentinmosque;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.estimote.sdk.SystemRequirementsChecker;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set initial date
        TextView TextView = (TextView) findViewById(R.id.DATE);
        String Date = DateFormat.getDateTimeInstance().format(new Date());
        TextView.setText(Date);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                changeLayout();
                            }
                        });
                    } catch (Exception e) {

                    }
                }
            }
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }


    public void changeLayout() {
        if (MyApplication.isFound) {
            setContentView(R.layout.my_application);
            TextView TextView = (TextView) findViewById(R.id.atDATE);
            String Date = DateFormat.getDateTimeInstance().format(new Date());
            TextView.setText(Date);

        } else {
            setContentView(R.layout.activity_main);
            TextView TextView = (TextView) findViewById(R.id.DATE);
            String Date = DateFormat.getDateTimeInstance().format(new Date());
            TextView.setText(Date);
        }

    }
}