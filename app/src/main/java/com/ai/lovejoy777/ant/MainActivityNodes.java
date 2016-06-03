package com.ai.lovejoy777.ant;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ai.lovejoy777.ant.activities.AboutActivity;

/**
 * Created by lovejoy777 on 03/10/15.
 */
public class MainActivityNodes extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    public final static String KEY_EXTRA_NODE_ID = "KEY_EXTRA_NODE_ID";
    public final static String KEY_EXTRA_NODE_ADDRESS = "KEY_EXTRA_NODE_ADDRESS";
    public final static String KEY_EXTRA_NODE_RSADDRESS = "KEY_EXTRA_NODE_RSADDRESS";
    public final static String KEY_EXTRA_NODE_TYPE = "KEY_EXTRA_NODE_TYPE";
    public final static String KEY_EXTRA_NODE_SWNUM = "KEY_EXTRA_NODE_SWNUM";
    public final static String KEY_EXTRA_NODE_SW1 = "KEY_EXTRA_NODE_SW1";
    public final static String KEY_EXTRA_NODE_SW2 = "KEY_EXTRA_NODE_SW2";
    public final static String KEY_EXTRA_NODE_SW3 = "KEY_EXTRA_NODE_SW3";
    public final static String KEY_EXTRA_NODE_SW4 = "KEY_EXTRA_NODE_SW4";
    public final static String KEY_EXTRA_NODE_NAME = "KEY_EXTRA_NODE_NAME";
    public final static String KEY_EXTRA_BASE_ID = "KEY_EXTRA_BASE_ID";
    public final static String KEY_EXTRA_BASE_NAME = "KEY_EXTRA_BASE_NAME";
    public final static String KEY_EXTRA_BASE_LOCALIP = "KEY_EXTRA_BASE_LOCALIP";
    public final static String KEY_EXTRA_BASE_PORT = "KEY_EXTRA_BASE_PORT";

    private ListView listView;
    ExampleDBHelper dbHelper;

    RelativeLayout MRL1;
    Toolbar toolBar;
    ListView listViewNode;

    TextView titleTextView;

    int baseID;
    String baseName;
    String baseLocalip;
    String basePort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_node);

        loadToolbarNavDrawer();

        baseID = getIntent().getIntExtra(MainActivity.KEY_EXTRA_BASE_ID, 0);
        baseName = getIntent().getStringExtra(MainActivity.KEY_EXTRA_BASE_NAME);
        baseLocalip = getIntent().getStringExtra(MainActivity.KEY_EXTRA_BASE_LOCALIP);
        basePort = getIntent().getStringExtra(MainActivity.KEY_EXTRA_BASE_PORT);

        baseID = getIntent().getIntExtra(CreateOrEditNodesActivity.KEY_EXTRA_BASE_ID, 0);
        baseName = getIntent().getStringExtra(CreateOrEditNodesActivity.KEY_EXTRA_BASE_NAME);

        //Toast.makeText(getApplicationContext(), "Trip " + tripName, Toast.LENGTH_SHORT).show();

        MRL1 = (RelativeLayout) findViewById(R.id.MRL1);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        listViewNode = (ListView) findViewById(R.id.listViewNode);
        titleTextView.setText("" + baseName);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivityNodes.this, CreateOrEditNodesActivity.class);

                intent.putExtra(KEY_EXTRA_NODE_ID, 0);
                intent.putExtra(KEY_EXTRA_BASE_ID, baseID);
                intent.putExtra(KEY_EXTRA_BASE_NAME, baseName);
                Bundle bndlanimation =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                startActivity(intent, bndlanimation);
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
            }
        });

        dbHelper = new ExampleDBHelper(this);

        populateListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) MainActivityNodes.this.listView.getItemAtPosition(position);
                int nodeID = itemCursor.getInt(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_ID));
                String nodeType = "" + itemCursor.getString(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_TYPE));
                String nodeAddress = "" + itemCursor.getString(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_ADDRESS));
                String nodeRSAddress = "" + itemCursor.getString(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_RSADDRESS));
                String nodeName = "" + itemCursor.getString(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_NAME));
                String nodeSwnum = "" + itemCursor.getString(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_SWNUM));
                String nodeSw1 = "" + itemCursor.getString(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_SW1));
                String nodeSw2 = "" + itemCursor.getString(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_SW2));
                String nodeSw3 = "" + itemCursor.getString(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_SW3));
                String nodeSw4 = "" + itemCursor.getString(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_SW4));


                Intent intent = new Intent(getApplicationContext(), Node.class);
                intent.putExtra(KEY_EXTRA_NODE_ID, nodeID);
                intent.putExtra(KEY_EXTRA_BASE_ID, baseID);
                intent.putExtra(KEY_EXTRA_BASE_LOCALIP, baseLocalip);
                intent.putExtra(KEY_EXTRA_BASE_PORT, basePort);
                intent.putExtra(KEY_EXTRA_NODE_NAME, nodeName);
                intent.putExtra(KEY_EXTRA_NODE_ADDRESS, nodeAddress);
                intent.putExtra(KEY_EXTRA_NODE_RSADDRESS, nodeRSAddress);
                intent.putExtra(KEY_EXTRA_NODE_TYPE, nodeType);
                intent.putExtra(KEY_EXTRA_NODE_SWNUM, nodeSwnum);
                intent.putExtra(KEY_EXTRA_NODE_SW1, nodeSw1);
                intent.putExtra(KEY_EXTRA_NODE_SW2, nodeSw2);
                intent.putExtra(KEY_EXTRA_NODE_SW3, nodeSw3);
                intent.putExtra(KEY_EXTRA_NODE_SW4, nodeSw4);
                Bundle bndlanim =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                startActivity(intent, bndlanim);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> listView, View view,
                                           int position, long id) {
                Cursor itemCursor = (Cursor) MainActivityNodes.this.listView.getItemAtPosition(position);
                int nodeID = itemCursor.getInt(itemCursor.getColumnIndex(ExampleDBHelper.NODE_COLUMN_ID));
                Intent intent = new Intent(getApplicationContext(), CreateOrEditNodesActivity.class);
                intent.putExtra(KEY_EXTRA_NODE_ID, nodeID);
                Bundle bndlanim =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                startActivity(intent, bndlanim);
                return true;
            }
        });
    }

    private void populateListView() {
        final Cursor cursor = dbHelper.getBaseNode(baseID);
        String[] columns = new String[] {
                ExampleDBHelper.NODE_COLUMN_ID,
                ExampleDBHelper.NODE_COLUMN_NAME,
                ExampleDBHelper.NODE_COLUMN_ADDRESS,
                ExampleDBHelper.NODE_COLUMN_RSADDRESS,
                ExampleDBHelper.NODE_COLUMN_BASE_ID

        };
        int [] widgets = new int[] {
                R.id.nodeID,
                R.id.nodeName
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.node_info,
                cursor, columns, widgets, 0);
        listView = (ListView)findViewById(R.id.listViewNode);
        listView.setAdapter(cursorAdapter);
    }

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
                                getSupportActionBar().setElevation(2);
                                break;
                            case R.id.nav_about:
                                Intent about = new Intent(MainActivityNodes.this, AboutActivity.class);
                                startActivity(about, bndlanimation);
                                break;

                            case R.id.nav_tutorial:
                                Intent test = new Intent(MainActivityNodes.this, Tutorial.class);
                                startActivity(test, bndlanimation);
                                break;

                            case R.id.nav_timers:
                                Intent timers = new Intent(MainActivityNodes.this, ReminderListActivity.class);
                                startActivity(timers, bndlanimation);
                                break;
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back2, R.anim.back1);
    }
}
