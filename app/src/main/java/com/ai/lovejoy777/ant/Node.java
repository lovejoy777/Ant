package com.ai.lovejoy777.ant;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.lovejoy777.ant.activities.AboutActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.natasa.progresspercent.CircularProgress;
import com.natasa.progresspercent.OnProgressTrackListener;

/**
 * Created by steve on 28/03/16.
 */
public class Node extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    BaseNodeDBHelper dbHelper;


    private CircularProgress circularProgress;
    private DrawerLayout mDrawerLayout;
    Toolbar toolBar;

    // for switches & heating layouts
    SwitchCompat sw1;
    ImageButton clockBtnsw1;
    SwitchCompat sw2;
    ImageButton clockBtnsw2;
    SwitchCompat sw3;
    ImageButton clockBtnsw3;
    SwitchCompat sw4;
    ImageButton clockBtnsw4;
    Button getTempBtn;
    TextView titleTV;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    /**
    // for dimmer layout
    TextView TVD1;
    TextView TVD2;
    TextView TVD3;
    TextView TVD4;
    TextView TVD1off;
    TextView TVD1on;
    TextView TVD2off;
    TextView TVD2on;
    TextView TVD3off;
    TextView TVD3on;
    TextView TVD4off;
    TextView TVD4on;
    SeekBar seekBarSw1 = null;
    SeekBar seekBarSw2 = null;
    SeekBar seekBarSw3 = null;
    SeekBar seekBarSw4 = null;

    // layouts for removing dimmers
    LinearLayout LDS1;
    LinearLayout LDS2;
    LinearLayout LDS3;
    LinearLayout LDS4;
    LinearLayout LDSB1;
    LinearLayout LDSB2;
    LinearLayout LDSB3;
    LinearLayout LDSB4;
     */

    // temperature return data
    TextView TVin;
    TextView tempTV;

    // To send and receive data to/from amica node v1
    DatagramSocket d1, d2;
    InetAddress ip;
    DatagramPacket send, rec;
    String modifiedSentence;

    // code to send for return Temperature
    String getTemp = ".493";

    // switch on & off codes
    String code1 = ".413";
    String code2 = ".423";
    String code3 = ".433";
    String code4 = ".443";
    String code5 = ".453";
    String code6 = ".463";
    String code7 = ".473";
    String code8 = ".483";

    int nodeID;
    String nodeName;
    String nodeAddress;
    String nodeRSAddress;
    String nodeType;
    String nodeSwnum;
    String nodeSw1;
    String nodeSw2;
    String nodeSw3;
    String nodeSw4;
    String nodeBase_ID;
    String nodeBase_Name;
    String nodeBase_Localip;
    String nodeBase_Port;

    String baseNodeName;

    public void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        dbHelper = new BaseNodeDBHelper(this);

        // get node id from MainActivityNodes
        nodeID = getIntent().getIntExtra(MainActivityNodes.KEY_EXTRA_NODE_ID, 0);
        nodeID = getIntent().getIntExtra(WidgetConfig.KEY_EXTRA_NODE_ID, 0);
       // Toast.makeText(getApplicationContext(), "node id = " + nodeID, Toast.LENGTH_LONG).show();

        // get all node data
        Cursor rs = dbHelper.getNode(nodeID);
        rs.moveToFirst();
        nodeName = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_NAME));
        nodeAddress = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_ADDRESS));
        nodeRSAddress = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_RSADDRESS));
        nodeType = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_TYPE));
        nodeSwnum = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_SWNUM));
        nodeSw1 = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_SW1));
        nodeSw2 = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_SW2));
        nodeSw3 = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_SW3));
        nodeSw4 = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_SW4));
        nodeBase_ID = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_BASE_ID));
        nodeBase_Name = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_BASE_NAME));
        nodeBase_Localip = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_BASE_LOCALIP));
        nodeBase_Port = rs.getString(rs.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_BASE_PORT));
        if (!rs.isClosed()) {
            rs.close();
        }

        // name for timers
        baseNodeName = nodeBase_Name + " " + nodeName;

        // NODE SWITCHES
        if (nodeType.equals("Switch")) {
            setContentView(R.layout.node);
            loadToolbarNavDrawer();

            toolBar = (Toolbar) findViewById(R.id.toolbar);

            sw1 = (SwitchCompat) findViewById(R.id.sw1);
            clockBtnsw1 = (ImageButton) findViewById(R.id.clockBtnsw1);
            sw2 = (SwitchCompat) findViewById(R.id.sw2);
            clockBtnsw2 = (ImageButton) findViewById(R.id.clockBtnsw2);
            sw3 = (SwitchCompat) findViewById(R.id.sw3);
            clockBtnsw3 = (ImageButton) findViewById(R.id.clockBtnsw3);
            sw4 = (SwitchCompat) findViewById(R.id.sw4);
            clockBtnsw4 = (ImageButton) findViewById(R.id.clockBtnsw4);
            TVin = (TextView) findViewById(R.id.TVin);

            titleTV = (TextView) findViewById(R.id.titleTV);
            textView1 = (TextView) findViewById(R.id.textView1);
            textView2 = (TextView) findViewById(R.id.textView2);
            textView3 = (TextView) findViewById(R.id.textView3);
            textView4 = (TextView) findViewById(R.id.textView4);
            titleTV.setText(baseNodeName);

            sw1.setVisibility(View.GONE);
            clockBtnsw1.setVisibility(View.GONE);
            sw2.setVisibility(View.GONE);
            clockBtnsw2.setVisibility(View.GONE);
            sw3.setVisibility(View.GONE);
            clockBtnsw3.setVisibility(View.GONE);
            sw4.setVisibility(View.GONE);
            clockBtnsw4.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
            textView4.setVisibility(View.GONE);

            // sets the number of switches shown
            if (nodeSwnum.equals("1")) {
                sw1.setVisibility(View.VISIBLE);
                clockBtnsw1.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
            }
            if (nodeSwnum.equals("2")) {
                sw1.setVisibility(View.VISIBLE);
                clockBtnsw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.VISIBLE);
                clockBtnsw2.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
            }
            if (nodeSwnum.equals("3")) {
                sw1.setVisibility(View.VISIBLE);
                clockBtnsw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.VISIBLE);
                clockBtnsw2.setVisibility(View.VISIBLE);
                sw3.setVisibility(View.VISIBLE);
                clockBtnsw3.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
            }
            if (nodeSwnum.equals("4")) {
                sw1.setVisibility(View.VISIBLE);
                clockBtnsw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.VISIBLE);
                clockBtnsw2.setVisibility(View.VISIBLE);
                sw3.setVisibility(View.VISIBLE);
                clockBtnsw3.setVisibility(View.VISIBLE);
                sw4.setVisibility(View.VISIBLE);
                clockBtnsw4.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.VISIBLE);
            }

            titleTV.setText(baseNodeName);
            textView1.setText(nodeSw1);
            textView2.setText(nodeSw2);
            textView3.setText(nodeSw3);
            textView4.setText(nodeSw4);

            // NODE SWITCH 1

            // SetClock
            clockBtnsw1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Node.this, R.style.MyAlertDialogStyle);
                    builder.setInverseBackgroundForced(true)
                            .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw1 + " On");
                                    savePrefs1("SWCODE", code1);
                                    createTimer();
                                }
                            })
                            .setNegativeButton("OFF", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw1 + " Off");
                                    savePrefs1("SWCODE", code2);
                                    createTimer();
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Set On or Off");
                    d.show();
                }
            });

            sw1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (sw1.getId()) {
                        case R.id.sw1:
                    }
                    if (sw1.isChecked()) {
                        try {
                            dataOut(nodeAddress + code1); // "0001"

                            TVin.setTextColor(Color.parseColor("#0277BD"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }

                    } else if (!sw1.isChecked()) {
                        try {
                            dataOut(nodeAddress + code2);  // "0002"

                            TVin.setTextColor(Color.parseColor("#FFFFFF"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }
                    }
                }
            });

            // NODE SWITCH 2
            // SetClock
            clockBtnsw2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Node.this, R.style.MyAlertDialogStyle);
                    builder.setInverseBackgroundForced(true)
                            .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw2 + " On");
                                    savePrefs1("SWCODE", code3);
                                    createTimer();
                                }
                            })
                            .setNegativeButton("OFF", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw2 + " Off");
                                    savePrefs1("SWCODE", code4);
                                    createTimer();
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Set On or Off");
                    d.show();
                }
            });

            sw2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (sw2.getId()) {
                        case R.id.sw2:
                    }
                    if (sw2.isChecked()) {
                        try {
                            dataOut(nodeAddress + code3); // "0003"
                            //sw2.setBackground(getResources().getDrawable(R.drawable.custom_btn_on));
                            TVin.setTextColor(Color.parseColor("#0277BD"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }

                    } else if (!sw2.isChecked()) {
                        try {
                            dataOut(nodeAddress + code4);  // "0004"
                           // sw2.setBackground(getResources().getDrawable(R.drawable.custom_btn_off));
                            TVin.setTextColor(Color.parseColor("#FFFFFF"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }
                    }
                }
            });

            // NODE SWITCH 3
            // SetClock
            clockBtnsw3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Node.this, R.style.MyAlertDialogStyle);
                    builder.setInverseBackgroundForced(true)
                            .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw3 + " On");
                                    savePrefs1("SWCODE", code5);
                                    createTimer();
                                }
                            })
                            .setNegativeButton("OFF", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw3 + " Off");
                                    savePrefs1("SWCODE", code6);
                                    createTimer();
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Set On or Off");
                    d.show();
                }
            });

            sw3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (sw3.getId()) {
                        case R.id.sw3:
                    }
                    if (sw3.isChecked()) {
                        try {
                            dataOut(nodeAddress + code5); // "0005"
                           // sw3.setBackground(getResources().getDrawable(R.drawable.custom_btn_on));
                            TVin.setTextColor(Color.parseColor("#0277BD"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }

                    } else if (!sw3.isChecked()) {
                        try {
                            dataOut(nodeAddress + code6);  // "0006"
                          //  sw3.setBackground(getResources().getDrawable(R.drawable.custom_btn_off));
                            TVin.setTextColor(Color.parseColor("#FFFFFF"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }
                    }
                }
            });

            // NODE SWITCH 4
            // SetClock
            clockBtnsw4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Node.this, R.style.MyAlertDialogStyle);
                    builder.setInverseBackgroundForced(true)
                            .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw4 + " On");
                                    savePrefs1("SWCODE", code7);
                                    createTimer();
                                }
                            })
                            .setNegativeButton("OFF", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw4 + " Off");
                                    savePrefs1("SWCODE", code8);
                                    createTimer();
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Set On or Off");
                    d.show();
                }
            });

            sw4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (sw4.getId()) {
                        case R.id.sw4:
                    }
                    if (sw4.isChecked()) {
                        try {
                            dataOut(nodeAddress + code7); // "0007"
                          //  sw4.setBackground(getResources().getDrawable(R.drawable.custom_btn_on));
                            TVin.setTextColor(Color.parseColor("#0277BD"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }

                    } else if (!sw4.isChecked()) {
                        try {
                            dataOut(nodeAddress + code8);  // "0008"
                          //  sw4.setBackground(getResources().getDrawable(R.drawable.custom_btn_off));
                            TVin.setTextColor(Color.parseColor("#FFFFFF"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }
                    }
                }
            });
        }
        // ROOM STAT
        if (nodeType.equals("RoomStat")) {
            setContentView(R.layout.heating);

            loadToolbarNavDrawer();
            initProgressBars();

            toolBar = (Toolbar) findViewById(R.id.toolbar);

            sw1 = (SwitchCompat) findViewById(R.id.sw1);
            clockBtnsw1 = (ImageButton) findViewById(R.id.clockBtnsw1);
            sw2 = (SwitchCompat) findViewById(R.id.sw2);
            clockBtnsw2 = (ImageButton) findViewById(R.id.clockBtnsw2);
            sw3 = (SwitchCompat) findViewById(R.id.sw3);
            clockBtnsw3 = (ImageButton) findViewById(R.id.clockBtnsw3);
            sw4 = (SwitchCompat) findViewById(R.id.sw4);
            clockBtnsw4 = (ImageButton) findViewById(R.id.clockBtnsw4);
            TVin = (TextView) findViewById(R.id.TVin);

            titleTV = (TextView) findViewById(R.id.titleTV);
            textView1 = (TextView) findViewById(R.id.textView1);
            textView2 = (TextView) findViewById(R.id.textView2);
            textView3 = (TextView) findViewById(R.id.textView3);
            textView4 = (TextView) findViewById(R.id.textView4);

            getTempBtn = (Button) findViewById(R.id.getTempBtn);
            tempTV = (TextView) findViewById(R.id.tempTV);
            titleTV.setText(baseNodeName);

            // auto get temp
            try {
                dataIn(nodeRSAddress + getTemp); // "9999"
            } catch (Exception e) {
                System.out.println("No connection");
            }

            sw1.setVisibility(View.GONE);
            clockBtnsw1.setVisibility(View.GONE);
            sw2.setVisibility(View.GONE);
            clockBtnsw2.setVisibility(View.GONE);
            sw3.setVisibility(View.GONE);
            clockBtnsw3.setVisibility(View.GONE);
            sw4.setVisibility(View.GONE);
            clockBtnsw4.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
            textView4.setVisibility(View.GONE);

            // sets the number of switches shown
            if (nodeSwnum.equals("1")) {
                sw1.setVisibility(View.VISIBLE);
                clockBtnsw1.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
            }
            if (nodeSwnum.equals("2")) {
                sw1.setVisibility(View.VISIBLE);
                clockBtnsw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.VISIBLE);
                clockBtnsw2.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
            }
            if (nodeSwnum.equals("3")) {
                sw1.setVisibility(View.VISIBLE);
                clockBtnsw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.VISIBLE);
                clockBtnsw2.setVisibility(View.VISIBLE);
                sw3.setVisibility(View.VISIBLE);
                clockBtnsw3.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
            }
            if (nodeSwnum.equals("4")) {
                sw1.setVisibility(View.VISIBLE);
                clockBtnsw1.setVisibility(View.VISIBLE);
                sw2.setVisibility(View.VISIBLE);
                clockBtnsw2.setVisibility(View.VISIBLE);
                sw3.setVisibility(View.VISIBLE);
                clockBtnsw3.setVisibility(View.VISIBLE);
                sw4.setVisibility(View.VISIBLE);
                clockBtnsw4.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.VISIBLE);
            }

            titleTV.setText(baseNodeName);
            textView1.setText(nodeSw1);
            textView2.setText(nodeSw2);
            textView3.setText(nodeSw3);
            textView4.setText(nodeSw4);

            // RS SWITCH 1

            // SetClock
            clockBtnsw1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Node.this, R.style.MyAlertDialogStyle);
                    builder.setInverseBackgroundForced(true)
                            .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw1 + " On");
                                    savePrefs1("SWCODE", code1);
                                    createTimer();
                                }
                            })
                            .setNegativeButton("OFF", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw1 + " Off");
                                    savePrefs1("SWCODE", code2);
                                    createTimer();
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Set On or Off");
                    d.show();
                }
            });
            sw1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (sw1.getId()) {
                        case R.id.sw1:
                    }
                    if (sw1.isChecked()) {
                        try {
                            dataOut(nodeAddress + code1); // "0001"
                           // sw1.setBackground(getResources().getDrawable(R.drawable.custom_btn_on));
                            TVin.setTextColor(Color.parseColor("#0277BD"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }

                    } else if (!sw1.isChecked()) {
                        try {
                            dataOut(nodeAddress + code2);  // "0002"
                           // sw1.setBackground(getResources().getDrawable(R.drawable.custom_btn_off));
                            TVin.setTextColor(Color.parseColor("#FFFFFF"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }
                    }
                }
            });

            // RS SWITCH 2
            // SetClock
            clockBtnsw2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Node.this, R.style.MyAlertDialogStyle);
                    builder.setInverseBackgroundForced(true)
                            .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw2 + " On");
                                    savePrefs1("SWCODE", code3);
                                    createTimer();
                                }
                            })
                            .setNegativeButton("OFF", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw2 + " Off");
                                    savePrefs1("SWCODE", code4);
                                    createTimer();
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Set On or Off");
                    d.show();
                }
            });
            sw2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (sw2.getId()) {
                        case R.id.sw2:
                    }
                    if (sw2.isChecked()) {
                        try {
                            dataOut(nodeAddress + code3); // "0003"
                           // sw2.setBackground(getResources().getDrawable(R.drawable.custom_btn_on));
                            TVin.setTextColor(Color.parseColor("#0277BD"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }

                    } else if (!sw2.isChecked()) {
                        try {
                            dataOut(nodeAddress + code4);  // "0004"
                           // sw2.setBackground(getResources().getDrawable(R.drawable.custom_btn_off));
                            TVin.setTextColor(Color.parseColor("#FFFFFF"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }
                    }
                }
            });

            // RS SWITCH 3
            // SetClock
            clockBtnsw3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Node.this, R.style.MyAlertDialogStyle);
                    builder.setInverseBackgroundForced(true)
                            .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw3 + " On");
                                    savePrefs1("SWCODE", code5);
                                    createTimer();
                                }
                            })
                            .setNegativeButton("OFF", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw3 + " Off");
                                    savePrefs1("SWCODE", code6);
                                    createTimer();
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Set On or Off");
                    d.show();
                }
            });
            sw3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (sw3.getId()) {
                        case R.id.sw3:
                    }
                    if (sw3.isChecked()) {
                        try {
                            dataOut(nodeAddress + code5); // "0005"
                          //  sw3.setBackground(getResources().getDrawable(R.drawable.custom_btn_on));
                            TVin.setTextColor(Color.parseColor("#0277BD"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }

                    } else if (!sw3.isChecked()) {
                        try {
                            dataOut(nodeAddress + code6);  // "0006"
                          //  sw3.setBackground(getResources().getDrawable(R.drawable.custom_btn_off));
                            TVin.setTextColor(Color.parseColor("#FFFFFF"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }
                    }
                }
            });

            // RS SWITCH 4
            // SetClock
            clockBtnsw4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Node.this, R.style.MyAlertDialogStyle);
                    builder.setInverseBackgroundForced(true)
                            .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw4 + " On");
                                    savePrefs1("SWCODE", code7);
                                    createTimer();
                                }
                            })
                            .setNegativeButton("OFF", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    savePrefs1("SWNAME", nodeSw4 + " Off");
                                    savePrefs1("SWCODE", code8);
                                    createTimer();
                                }
                            });
                    AlertDialog d = builder.create();
                    d.setTitle("Set On or Off");
                    d.show();
                }
            });
            sw4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (sw4.getId()) {
                        case R.id.sw4:
                    }
                    if (sw4.isChecked()) {
                        try {
                            dataOut(nodeAddress + code7); // "0007"
                          //  sw4.setBackground(getResources().getDrawable(R.drawable.custom_btn_on));
                            TVin.setTextColor(Color.parseColor("#0277BD"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }

                    } else if (!sw4.isChecked()) {
                        try {
                            dataOut(nodeAddress + code8);  // "0008"
                           // sw4.setBackground(getResources().getDrawable(R.drawable.custom_btn_off));
                            TVin.setTextColor(Color.parseColor("#FFFFFF"));
                        } catch (Exception e) {
                            System.out.println("No connection");
                        }
                    }
                }
            });

            // GET TEMP
            getTempBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        dataIn(nodeRSAddress + getTemp); // "9999"
                    } catch (Exception e) {
                        System.out.println("No connection");
                    }
                }
            });
        }

        /**                                         // DIMMER SWITCHES
         if (nodetypen1.equals("Dimmer")) {
         setContentView(R.layout.dimmer);
         loadToolbarNavDrawer();
         toolBar = (Toolbar) findViewById(R.id.toolbar);

         TVin = (TextView) findViewById(R.id.TVin);

         LDS1 = (LinearLayout) findViewById(R.id.LDS1);
         LDS2 = (LinearLayout) findViewById(R.id.LDS2);
         LDS3 = (LinearLayout) findViewById(R.id.LDS3);
         LDS4 = (LinearLayout) findViewById(R.id.LDS4);
         LDSB1 = (LinearLayout) findViewById(R.id.LDSB1);
         LDSB2 = (LinearLayout) findViewById(R.id.LDSB2);
         LDSB3 = (LinearLayout) findViewById(R.id.LDSB3);
         LDSB4 = (LinearLayout) findViewById(R.id.LDSB4);

         titleTV = (TextView) findViewById(R.id.titleTV);
         TVD1 = (TextView) findViewById(R.id.TVD1);
         TVD2 = (TextView) findViewById(R.id.TVD2);
         TVD3 = (TextView) findViewById(R.id.TVD3);
         TVD4 = (TextView) findViewById(R.id.TVD4);

         TVD1off = (TextView) findViewById(R.id.TVD1off);
         TVD2off = (TextView) findViewById(R.id.TVD2off);
         TVD3off = (TextView) findViewById(R.id.TVD3off);
         TVD4off = (TextView) findViewById(R.id.TVD4off);
         TVD1on = (TextView) findViewById(R.id.TVD1on);
         TVD2on = (TextView) findViewById(R.id.TVD2on);
         TVD3on = (TextView) findViewById(R.id.TVD3on);
         TVD4on = (TextView) findViewById(R.id.TVD4on);

         seekBarSw1 = (SeekBar) findViewById(R.id.seekBarSw1);
         seekBarSw2 = (SeekBar) findViewById(R.id.seekBarSw2);
         seekBarSw3 = (SeekBar) findViewById(R.id.seekBarSw3);
         seekBarSw4 = (SeekBar) findViewById(R.id.seekBarSw4);

         titleTV.setText("" + nodenamen1);

         LDS1.setVisibility(View.GONE);
         LDS2.setVisibility(View.GONE);
         LDS3.setVisibility(View.GONE);
         LDS4.setVisibility(View.GONE);
         LDSB1.setVisibility(View.GONE);
         LDSB2.setVisibility(View.GONE);
         LDSB3.setVisibility(View.GONE);
         LDSB4.setVisibility(View.GONE);

         // sets the number of switches shown
         if (swnumn1.equals("1")) {
         LDS1.setVisibility(View.VISIBLE);
         LDSB1.setVisibility(View.VISIBLE);

         }
         if (swnumn1.equals("2")) {
         LDS1.setVisibility(View.VISIBLE);
         LDS2.setVisibility(View.VISIBLE);
         LDSB1.setVisibility(View.VISIBLE);
         LDSB2.setVisibility(View.VISIBLE);
         }
         if (swnumn1.equals("3")) {
         LDS1.setVisibility(View.VISIBLE);
         LDS2.setVisibility(View.VISIBLE);
         LDS3.setVisibility(View.VISIBLE);
         LDSB1.setVisibility(View.VISIBLE);
         LDSB2.setVisibility(View.VISIBLE);
         LDSB3.setVisibility(View.VISIBLE);
         }
         if (swnumn1.equals("4")) {
         LDS1.setVisibility(View.VISIBLE);
         LDS2.setVisibility(View.VISIBLE);
         LDS3.setVisibility(View.VISIBLE);
         LDS4.setVisibility(View.VISIBLE);
         LDSB1.setVisibility(View.VISIBLE);
         LDSB2.setVisibility(View.VISIBLE);
         LDSB3.setVisibility(View.VISIBLE);
         LDSB4.setVisibility(View.VISIBLE);
         }

         titleTV.setText(nodenamen1);
         TVD1.setText(sw1namen1);
         TVD2.setText(sw2namen1);
         TVD3.setText(sw3namen1);
         TVD4.setText(sw4namen1);

         // DIMMER 1
         seekBarSw1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         int sw1DimVal = 0;
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
         sw1DimVal = progress;
         }
         public void onStartTrackingTouch(SeekBar seekBar) {
         // TODO Auto-generated method stub
         }
         public void onStopTrackingTouch(SeekBar seekBar) {
         Toast.makeText(getApplicationContext(), "seek bar progress:" + sw1DimVal, Toast.LENGTH_SHORT).show();
         if (sw1DimVal > 50 ) {
         try {
         dataOut(addressn1 + code1); // "on"
         TVin.setTextColor(Color.parseColor("#0277BD"));
         } catch (Exception e) {
         System.out.println("No connection");
         }
         }
         if (sw1DimVal < 50 ) {
         try {
         dataOut(addressn1 + code2);  // "off"
         TVin.setTextColor(Color.parseColor("#e57373"));
         } catch (Exception e) {
         System.out.println("No connection");
         }
         }
         }
         });
         // DIMMER 2
         seekBarSw2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         int sw2DimVal = 0;
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
         sw2DimVal = progress;
         }
         public void onStartTrackingTouch(SeekBar seekBar) {
         // TODO Auto-generated method stub
         }
         public void onStopTrackingTouch(SeekBar seekBar) {
         Toast.makeText(getApplicationContext(), "seek bar progress:" + sw2DimVal, Toast.LENGTH_SHORT).show();
         if (sw2DimVal > 50 ) {
         try {
         dataOut(addressn1 + code3); // "on"
         TVin.setTextColor(Color.parseColor("#0277BD"));
         } catch (Exception e) {
         System.out.println("No connection");
         }
         }
         if (sw2DimVal < 50 ) {
         try {
         dataOut(addressn1 + code4);  // "off"
         TVin.setTextColor(Color.parseColor("#e57373"));
         } catch (Exception e) {
         System.out.println("No connection");
         }
         }
         }
         });
         // DIMMER 3
         seekBarSw3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         int sw3DimVal = 0;
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
         sw3DimVal = progress;
         }
         public void onStartTrackingTouch(SeekBar seekBar) {
         // TODO Auto-generated method stub
         }
         public void onStopTrackingTouch(SeekBar seekBar) {
         Toast.makeText(getApplicationContext(), "seek bar progress:" + sw3DimVal, Toast.LENGTH_SHORT).show();
         if (sw3DimVal > 50 ) {
         try {
         dataOut(addressn1 + code5); // "on"
         TVin.setTextColor(Color.parseColor("#0277BD"));
         } catch (Exception e) {
         System.out.println("No connection");
         }
         }
         if (sw3DimVal < 50 ) {
         try {
         dataOut(addressn1 + code6);  // "off"
         TVin.setTextColor(Color.parseColor("#e57373"));
         } catch (Exception e) {
         System.out.println("No connection");
         }
         }
         }
         });
         // DIMMER 4
         seekBarSw4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
         int sw4DimVal = 0;
         public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
         sw4DimVal = progress;
         }
         public void onStartTrackingTouch(SeekBar seekBar) {
         // TODO Auto-generated method stub
         }
         public void onStopTrackingTouch(SeekBar seekBar) {
         Toast.makeText(getApplicationContext(), "seek bar progress:" + sw4DimVal, Toast.LENGTH_SHORT).show();
         if (sw4DimVal > 50 ) {
         try {
         dataOut(addressn1 + code7); // "on"
         TVin.setTextColor(Color.parseColor("#0277BD"));
         } catch (Exception e) {
         System.out.println("No connection");
         }
         }
         if (sw4DimVal < 50 ) {
         try {
         dataOut(addressn1 + code8);  // "off"
         TVin.setTextColor(Color.parseColor("#e57373"));
         } catch (Exception e) {
         System.out.println("No connection");
         }
         }
         }
         });

         } */
    }

    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected())
            return true;
        return false;
    }


    public void dataOut(String s) throws Exception {
        int port = Integer.valueOf(nodeBase_Port);


        byte[] b = (s.getBytes());
        byte[] receiveData = new byte[1024];
        if (isOnline()) {

            ip = InetAddress.getByName(nodeBase_Localip);
            d1 = new DatagramSocket();
            try {
                send = new DatagramPacket(b, b.length, ip, port);
                d1.send(send);
                d1.setSoTimeout(3000);

                rec = new DatagramPacket(receiveData, receiveData.length);
                d1.receive(rec);
                d1.setSoTimeout(3000);
                modifiedSentence = new String(rec.getData());

                //if (modifiedSentence.equals("On")) {
                  //  sw1.setBackground(getResources().getDrawable(R.drawable.custom_btn_on));
                //}
                //if (modifiedSentence.equals("Off")) {
                  //  sw1.setBackground(getResources().getDrawable(R.drawable.custom_btn_off));
               // }

                TVin.setText(modifiedSentence);
                d1.close();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_SHORT).show();
        }
    } // end of dataOut

    public void dataIn(String s) throws Exception {
        int port = Integer.valueOf(nodeBase_Port);

        byte[] b = (s.getBytes());
        byte[] receiveData = new byte[1024];

        if (isOnline()) {

            ip = InetAddress.getByName(nodeBase_Localip);
            d2 = new DatagramSocket();
            try {
                // Send Data
                send = new DatagramPacket(b, b.length, ip, port);
                d2.send(send);
                d2.setSoTimeout(3000); // time to send data

                // Receive Data
                rec = new DatagramPacket(receiveData, receiveData.length);
                d2.receive(rec);
                d2.setSoTimeout(3000); // time to receive data
                modifiedSentence = new String(rec.getData());
                tempTV.setText(modifiedSentence + " C");


                Float f = Float.parseFloat(modifiedSentence);
                int i = Math.round(f);
                int progress = i * 2;
                if (progress < 50) {
                    circularProgress.setProgressColor(Color.parseColor("#0277BD"));

                } else {
                    circularProgress.setProgressColor(Color.parseColor("#FF4081"));
                }
                circularProgress.setProgress(progress);
                d2.close();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_SHORT).show();
        }
    } // end of dataIn

    private void loadToolbarNavDrawer() {
        //set Toolbar
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //set NavigationDrawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    //navigationDrawerIcon Onclick
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                mDrawerLayout.openDrawer(GravityCompat.START);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //set NavigationDrawerContent
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();
                        menuItem.setChecked(true);
                        Bundle bndlanimation =
                                ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.nav_home:
                                mDrawerLayout.closeDrawers();
                                getSupportActionBar().setElevation(0);
                                break;
                            case R.id.nav_about:
                                Intent about = new Intent(Node.this, AboutActivity.class);
                                startActivity(about, bndlanimation);
                                break;

                            case R.id.nav_tutorial:
                                Intent test = new Intent(Node.this, Tutorial.class);
                                startActivity(test, bndlanimation);
                                break;

                            case R.id.nav_timers:
                                Intent timers = new Intent(Node.this, TimerListActivity.class);
                                startActivity(timers, bndlanimation);
                                break;
                        }
                        return false;
                    }
                });
    }

    private void initProgressBars() {

        circularProgress = (CircularProgress) findViewById(R.id.circular);
        circularProgress.setTypeface("Roboto-Regular.ttf");
        circularProgress.setTextSize(0);

        //methods that can be used for both progress views

        // circularProgress.setProgressColor(Color.parseColor("#0277BD"));
        //circularProgress.setTypeface("Roboto-Regular.ttf");
        // lineProgress.setBackgroundColor(Color.LTGRAY);
        // lineProgress.setProgressColor(Color.GREEN);
        // lineProgress.setTextColor(Color.BLACK);
        // circularProgress.setTextSize(30);
        // circularProgress.setBackgroundStrokeWidth(10);
        // circularProgress.setProgressStrokeWidth(15);
        //circularProgress.setRoundEdge(true);
        //circularProgress.setShadow(true);
        circularProgress.setOnProgressTrackListener(new OnProgressTrackListener() {
            @Override
            public void onProgressFinish() {
                //circularProgress.resetProgress();
            }

            @Override
            public void onProgressUpdate(int progress) {
            }
        });
    }

    private void createTimer() {
        Intent i = new Intent(this, TimerEditActivity.class);

        startActivityForResult(i, ACTIVITY_CREATE);
    }

    private void savePrefs1(String key, String value) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back2, R.anim.back1);
    }
}