
package com.ai.lovejoy777.ant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


public class TimerEditActivity extends AppCompatActivity {

    // Dialog Constants
    private static final int DATE_PICKER_DIALOG = 0;
    private static final int TIME_PICKER_DIALOG = 1;

    Toolbar toolBar;

    // Date Format
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    public final static String KEY_TIMER_REPEAT = "KEY_TIMER_REPEAT";

    private EditText mETName;
    private EditText mETSWName;
    private EditText mETAddress;
    private EditText mETCode;
    private EditText mETLocalIP;
    private EditText mETPort;
    private EditText mETDate;
    private EditText mETTime;
    private EditText mETRepeat;
    private Button mDateButton;
    private Button mTimeButton;
    private SwitchCompat mDaySwitch;
    private SwitchCompat mWeekSwitch;
    private Button mConfirmButton;
    private Long mRowId;
    private TimerDbAdapter mDbHelper;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new TimerDbAdapter(this);

        setContentView(R.layout.main_timer_edit);

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        mCalendar = Calendar.getInstance();
        mETName = (EditText) findViewById(R.id.etname);
        mETSWName = (EditText) findViewById(R.id.etswname);
        mETAddress = (EditText) findViewById(R.id.etaddress);
        mETCode = (EditText) findViewById(R.id.etcode);
        mETLocalIP = (EditText) findViewById(R.id.etlocalip);
        mETPort = (EditText) findViewById(R.id.etport);
        mETDate = (EditText) findViewById(R.id.etdate);
        mETTime = (EditText) findViewById(R.id.ettime);
        mETRepeat = (EditText) findViewById(R.id.etrepeat);
        mDateButton = (Button) findViewById(R.id.reminder_date);
        mTimeButton = (Button) findViewById(R.id.reminder_time);
        mDaySwitch = (SwitchCompat) findViewById(R.id.dayswitch);
        mWeekSwitch = (SwitchCompat) findViewById(R.id.weekswitch);

        mConfirmButton = (Button) findViewById(R.id.confirm);

        mRowId = savedInstanceState != null ? savedInstanceState.getLong(TimerDbAdapter.KEY_ROWID)
                : null;

        registerButtonListenersAndSetDefaultText();
    }

    private void setRowIdFromIntent() {
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(TimerDbAdapter.KEY_ROWID)
                    : null;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_DIALOG:
                return showDatePicker();
            case TIME_PICKER_DIALOG:
                return showTimePicker();
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog showDatePicker() {

        DatePickerDialog datePicker = new DatePickerDialog(TimerEditActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateETText();
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private TimePickerDialog showTimePicker() {

        TimePickerDialog timePicker = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                updateTimeETText();
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);

        return timePicker;
    }

    private void registerButtonListenersAndSetDefaultText() {

        final String dayOn = "Daily";
        final String weekOn = "Weekly";

        mDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_DIALOG);

            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_PICKER_DIALOG);
            }
        });

        //set the switch to Off
        mDaySwitch.setChecked(false);
        mWeekSwitch.setChecked(false);
        //attach a listener to check for changes in state
        mDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    mETRepeat.setText(dayOn);
                    mWeekSwitch.setChecked(false);

                }
            }
        });

        mWeekSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    mETRepeat.setText(weekOn);
                    mDaySwitch.setChecked(false);
                }
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveState();
                setResult(RESULT_OK);
                Toast.makeText(TimerEditActivity.this, getString(R.string.task_saved_message), Toast.LENGTH_SHORT).show();
                finish();



            }

        });

        updateDateETText();
        updateTimeETText();
    }

    private void populateFields() {
        // Only populate the text boxes and change the calendar date
        // if the row is not null from the database.
        if (mRowId != null) {
            Cursor reminder = mDbHelper.fetchTimer(mRowId);
            startManagingCursor(reminder);
            mETName.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(TimerDbAdapter.KEY_NAME)));
            mETSWName.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(TimerDbAdapter.KEY_SWNAME)));
            mETAddress.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(TimerDbAdapter.KEY_ADDRESS)));
            mETCode.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(TimerDbAdapter.KEY_CODE)));
            mETLocalIP.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(TimerDbAdapter.KEY_LOCALIP)));
            mETPort.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(TimerDbAdapter.KEY_PORT)));
            mETRepeat.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(TimerDbAdapter.KEY_REPEAT)));

            String repeatText = (reminder.getString(
                    reminder.getColumnIndexOrThrow(TimerDbAdapter.KEY_REPEAT)));

            // set switches
            if (repeatText.equals("Daily")) {
                mDaySwitch.setChecked(true);
                mWeekSwitch.setChecked(false);

            }
            if (repeatText.equals("Weeklk")) {
                mDaySwitch.setChecked(false);
                mWeekSwitch.setChecked(true);
            }

            // Get the date from the database and format it for our use. 
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            Date date = null;
            try {
                String dateString = reminder.getString(reminder.getColumnIndexOrThrow(TimerDbAdapter.KEY_DATE_TIME));
                date = dateTimeFormat.parse(dateString);
                mCalendar.setTime(date);
            } catch (ParseException e) {
                Log.e("TimerEditActivity", e.getMessage(), e);
            }
        } else {
            // This is a new task - add defaults from preferences if set.
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String defaultTimeKey = getString(R.string.pref_default_time_from_now_key);
            String defaultTime = prefs.getString(defaultTimeKey, null);

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String name = sp.getString("NAME", null);
            String swname = sp.getString("SWNAME", null);
            String address = sp.getString("ADDRESS", null);
            String swCode = sp.getString("SWCODE", null);
            String localip = sp.getString("LOCALIP", null);
            String port = sp.getString("PORT", null);

            if (address != null)
                mETName.setText(name);
                mETSWName.setText(swname);
                mETAddress.setText(address);
                mETCode.setText(swCode);
                mETLocalIP.setText(localip);
                mETPort.setText(port);

            if (defaultTime != null)
                mCalendar.add(Calendar.MINUTE, Integer.parseInt(defaultTime));
        }
        updateDateETText();
        updateTimeETText();
    }

    private void updateTimeETText() {
        // Set the time edit text based upon the value from the database
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        String timeForETTime = timeFormat.format(mCalendar.getTime());
        mETTime.setText(timeForETTime);
    }

    private void updateDateETText() {
        // Set the date edit text based upon the value from the database
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForETDate = dateFormat.format(mCalendar.getTime());
        mETDate.setText(dateForETDate);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TimerDbAdapter.KEY_ROWID, mRowId);
    }

    private void saveState() {
        String name = mETName.getText().toString();
        String swname = mETSWName.getText().toString();
        String address = mETAddress.getText().toString();
        String code = mETCode.getText().toString();
        String localip = mETLocalIP.getText().toString();
        String port = mETPort.getText().toString();
        String repeat = mETRepeat.getText().toString();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        String timerDateTime = dateTimeFormat.format(mCalendar.getTime());

        if (mRowId == null) {

            long id = mDbHelper.createTimer(name, swname, address, code, localip, port, timerDateTime, repeat);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateTimer(mRowId, name, swname, address, code, localip, port, timerDateTime, repeat);
        }
        new TimerManager(this).setTimer(mRowId, name, swname, mCalendar, repeat);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDbHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDbHelper.open();
        setRowIdFromIntent();
        populateFields();
    }

    @Override
    public void onBackPressed() {
        mDbHelper.close();
        super.onBackPressed();
        overridePendingTransition(R.anim.back2, R.anim.back1);
    }
}