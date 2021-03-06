package com.ai.lovejoy777.ant;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimerListActivity extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;

    // Date Format
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    private TimerDbAdapter mDbHelper;
    private Calendar mCalendar;
    private DrawerLayout mDrawerLayout;
    private ListView listView;

    RelativeLayout MRL1;
    Toolbar toolBar;
    ListView list;
    TextView titleTextView;
    TextView text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_timers);

        loadToolbarNavDrawer();
        mCalendar = Calendar.getInstance();
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        MRL1 = (RelativeLayout) findViewById(R.id.MRL1);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        text4 = (TextView) findViewById(R.id.text4);


        list = (ListView) findViewById(R.id.list);

        titleTextView.setText("Timers");




        mDbHelper = new TimerDbAdapter(this);
        mDbHelper.open();

        populateListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) TimerListActivity.this.listView.getItemAtPosition(position);
                //int rowID = itemCursor.getInt(itemCursor.getColumnIndex(TimerDbAdapter.KEY_ROWID));

                Intent i = new Intent(getApplicationContext(), TimerEditActivity.class);
                i.putExtra(TimerDbAdapter.KEY_ROWID, id);
                startActivityForResult(i, ACTIVITY_EDIT);
                // Toast.makeText(getApplicationContext(), "Edit " + rowID, Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> listView, View view,
                                           int position, long id) {
                Cursor itemCursor = (Cursor) TimerListActivity.this.listView.getItemAtPosition(position);
                final int rowID = itemCursor.getInt(itemCursor.getColumnIndex(TimerDbAdapter.KEY_ROWID));

                AlertDialog.Builder builder = new AlertDialog.Builder(TimerListActivity.this, R.style.MyAlertDialogStyle);
                builder.setMessage(R.string.deleteTimer);
                builder.setInverseBackgroundForced(true)

                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                mDbHelper.deleteTimer(rowID); // deletes from the database
                                amDeleteTimer(rowID); // deletes from alarm manager
                                populateListView();
                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Delete Timer");
                d.show();

                return true;
            }
        });
    }

    private void amDeleteTimer(long rowID) {

        new TimerManager(this).deleteTimer(rowID);
    }

    private void populateListView() {

        final Cursor cursor = mDbHelper.fetchAllTimers();

        String[] from = new String[]{TimerDbAdapter.KEY_NAME, TimerDbAdapter.KEY_SWNAME, TimerDbAdapter.KEY_DATE_TIME, TimerDbAdapter.KEY_REPEAT};
        int[] to = new int[]{R.id.text1, R.id.text2, R.id.text3, R.id.text4 };
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.timer_info, cursor, from, to);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(cursorAdapter);

    }

    private void loadToolbarNavDrawer() {
        //set Toolbar
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set NavigationDrawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_timers_layout);
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

                            case R.id.nav_settings:
                                Intent settings = new Intent(TimerListActivity.this, TimerPreferences.class);
                                startActivity(settings, bndlanimation);
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
        mDbHelper.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        populateListView();
    }
}