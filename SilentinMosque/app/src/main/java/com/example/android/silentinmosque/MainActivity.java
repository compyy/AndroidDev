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

    public static String beaconUUID;
    public static int beaconMajor;
    public static int beaconMinor;
    public static String beaconNameAddress;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set initial date
        TextView TextView = (TextView) findViewById(R.id.DATE);
        String Date = DateFormat.getDateTimeInstance().format(new Date());
        TextView.setText(Date);

        beaconUUID = getPreferences(MODE_PRIVATE).getString("UUID", "B9407F30-F5F8-466E-AFF9-25556B57FE6D");
        beaconMajor = getPreferences(MODE_PRIVATE).getInt("Major", 30703);
        beaconMinor = getPreferences(MODE_PRIVATE).getInt("Minor", 24375);
        beaconNameAddress = getPreferences(MODE_PRIVATE).getString("NameAddress", "MOSQUE UTHMAN, 1930, Zaventem, Belgium");

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
            //update date
            TextView TextView = (TextView) findViewById(R.id.atDATE);
            String Date = DateFormat.getDateTimeInstance().format(new Date());
            TextView.setText(Date);

            //update Mosque
            TextView MosqueName = (TextView) findViewById(R.id.textMosque);
            MosqueName.setText(beaconNameAddress);

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
        final EditText editTextMosqueAddress = (EditText) promptView.findViewById(R.id.editTextDialogMosqueAddress);


        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getPreferences(MODE_PRIVATE).edit().putString("UUID", editTextUUID.getText().toString()).commit();
                        getPreferences(MODE_PRIVATE).edit().putInt("Major", Integer.parseInt((editTextMajor.getText().toString()))).commit();
                        getPreferences(MODE_PRIVATE).edit().putInt("Minor", Integer.parseInt((editTextMinor.getText().toString()))).commit();
                        getPreferences(MODE_PRIVATE).edit().putString("NameAddress", editTextMosqueAddress.getText().toString()).commit();

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