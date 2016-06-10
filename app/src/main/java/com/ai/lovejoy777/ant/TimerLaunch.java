package com.ai.lovejoy777.ant;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by steve on 02/06/16.
 */
public class TimerLaunch extends Activity {

    private TimerDbAdapter dbHelper;
    private Long mRowId;

    // To send and receive data to/from amica node v1
    DatagramSocket d1;
    InetAddress ip;
    DatagramPacket send;

    private ImageView myimage;
    TextView swName;
    TextView Name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_launch);
        dbHelper = new TimerDbAdapter(this);

        myimage = (ImageView) findViewById(R.id.image);
        Name = (TextView) findViewById(R.id.textName);
        swName = (TextView) findViewById(R.id.textswName);



        mRowId = savedInstanceState != null ? savedInstanceState.getLong(TimerDbAdapter.KEY_ROWID)
                : null;

        dbHelper.open();
        setRowIdFromIntent();

        Cursor ft = dbHelper.fetchTimer(mRowId);
        ft.moveToFirst();
        String name = ft.getString(ft.getColumnIndex(TimerDbAdapter.KEY_NAME));
        String swname = ft.getString(ft.getColumnIndex(TimerDbAdapter.KEY_SWNAME));
        if (!ft.isClosed()) {
            ft.close();
        }

        Name.setText(name);
        swName.setText(swname);

        sendCom();



        Thread timer = new Thread() {
            public void run() {
                try {
                    rotateAnimation(myimage);
                    //Display for 5 seconds
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {


                    // delete timer if it was a one off (not repeating).
                    Cursor ft1 = dbHelper.fetchTimer(mRowId);
                    ft1.moveToFirst();
                    String repeat = ft1.getString(ft1.getColumnIndex(TimerDbAdapter.KEY_REPEAT));
                    if (!ft1.isClosed()) {
                        ft1.close();
                    }
                    if (repeat.equals("Once")) {
                        dbHelper.deleteTimer(mRowId); // deletes from the database
                    }
                    dbHelper.close();
                    finish();
                }
            }
        };

        timer.start();
    }

    private void setRowIdFromIntent() {
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(TimerDbAdapter.KEY_ROWID)
                    : null;
        }
    }

    private void rotateAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        myimage.startAnimation(animation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper.open();
        setRowIdFromIntent();
        sendCom();
    }

    private void sendCom() {

        Cursor rs = dbHelper.fetchTimer(mRowId);
        rs.moveToFirst();
        String address = rs.getString(rs.getColumnIndex(TimerDbAdapter.KEY_ADDRESS));
        String code = rs.getString(rs.getColumnIndex(TimerDbAdapter.KEY_CODE));
        if (!rs.isClosed()) {
            rs.close();
        }
        try {
            dataOut(address + code); // "on"

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();
            System.out.println("No connection");
        }
    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected())
            return true;
        return false;
    }

    public void dataOut(String s) throws Exception {

        Cursor rs = dbHelper.fetchTimer(mRowId);
        rs.moveToFirst();
        String localip = rs.getString(rs.getColumnIndex(TimerDbAdapter.KEY_LOCALIP));
        String port = rs.getString(rs.getColumnIndex(TimerDbAdapter.KEY_PORT));
        String code = rs.getString(rs.getColumnIndex(TimerDbAdapter.KEY_CODE));
        if (!rs.isClosed()) {
            rs.close();
        }

        int port1 = Integer.valueOf(port);

        byte[] b = (s.getBytes());

        if (isOnline()) {

            ip = InetAddress.getByName(localip);
            d1 = new DatagramSocket();
            try {
                send = new DatagramPacket(b, b.length, ip, port1);
                d1.send(send);
                d1.setSoTimeout(3000);

                //Toast.makeText(getApplicationContext(), "Sw Code: " + code, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_SHORT).show();
        }
    } // end of dataOut
}