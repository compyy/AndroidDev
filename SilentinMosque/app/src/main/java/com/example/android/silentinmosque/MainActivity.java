package com.example.android.silentinmosque;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.estimote.sdk.SystemRequirementsChecker;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static String beaconUUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static int beaconMajor = 30703;
    public static int beaconMinor = 24375;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.enterData) {

            updateBeacon();
            return true;
        }
        if (id == R.id.appExit) {
            appExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void appExit() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void updateBeacon() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editTextUUID = (EditText) promptView.findViewById(R.id.editTextDialogUUID);
        final EditText editTextMajor = (EditText) promptView.findViewById(R.id.editTextDialogMajor);
        final EditText editTextMinor = (EditText) promptView.findViewById(R.id.editTextDialogMinor);


        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        beaconUUID = editTextUUID.getText().toString();
                        beaconMajor = Integer.parseInt(editTextMajor.getText().toString());
                        beaconMinor = Integer.parseInt(editTextMinor.getText().toString());


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}