
package com.ai.lovejoy777.ant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;


public class ReminderEditActivity extends AppCompatActivity {

	// 
	// Dialog Constants
	//
	private static final int DATE_PICKER_DIALOG = 0;
	private static final int TIME_PICKER_DIALOG = 1;

	Toolbar toolBar;
	
	// 
	// Date Format 
	//
	private static final String DATE_FORMAT = "yyyy-MM-dd"; 
	private static final String TIME_FORMAT = "kk:mm";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
	
	private EditText mETAddress;
    private EditText mETCode;
    private EditText mETLocalIP;
    private EditText mETPort;
	private EditText mETDate;
	private EditText mETTime;
    private Button mDateButton;
    private Button mTimeButton;
	private Switch mDailySwitch;
    private Button mConfirmButton;
    private Long mRowId;
	private Boolean mDailySW;
    private RemindersDbAdapter mDbHelper;
    private Calendar mCalendar;  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mDbHelper = new RemindersDbAdapter(this);
        
        setContentView(R.layout.main_timer_edit);

		toolBar = (Toolbar) findViewById(R.id.toolbar);
        
        mCalendar = Calendar.getInstance(); 
        mETAddress = (EditText) findViewById(R.id.etaddress);
        mETCode = (EditText) findViewById(R.id.etcode);
        mETLocalIP = (EditText) findViewById(R.id.etlocalip);
        mETPort = (EditText) findViewById(R.id.etport);
		mETDate = (EditText) findViewById(R.id.etdate);
		mETTime = (EditText) findViewById(R.id.ettime);
        mDateButton = (Button) findViewById(R.id.reminder_date);
        mTimeButton = (Button) findViewById(R.id.reminder_time);
		mDailySwitch = (Switch) findViewById(R.id.dailyswitch);

        mConfirmButton = (Button) findViewById(R.id.confirm);
       
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(RemindersDbAdapter.KEY_ROWID) 
                							: null;
      
        registerButtonListenersAndSetDefaultText();
    }

	private void setRowIdFromIntent() {
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();            
			mRowId = extras != null ? extras.getLong(RemindersDbAdapter.KEY_ROWID) 
									: null;
			
		}
	}
    

    
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch(id) {
    		case DATE_PICKER_DIALOG: 
    			return showDatePicker();
    		case TIME_PICKER_DIALOG: 
    			return showTimePicker(); 
    	}
    	return super.onCreateDialog(id);
    }
    
 	private DatePickerDialog showDatePicker() {
		
		
		DatePickerDialog datePicker = new DatePickerDialog(ReminderEditActivity.this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

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
		
    	TimePickerDialog timePicker = new TimePickerDialog(this, R.style.DialogTheme,  new TimePickerDialog.OnTimeSetListener() {

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

		//set the switch to ON
		mDailySwitch.setChecked(true);
		//attach a listener to check for changes in state
		mDailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {

				if(isChecked){

					mDailySW = true;

				}else{
					mDailySW = false;

				}

			}
		});





	mConfirmButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		saveState(); 
        		setResult(RESULT_OK);
        	    Toast.makeText(ReminderEditActivity.this, getString(R.string.task_saved_message), Toast.LENGTH_SHORT).show();
        	    finish(); 
        	}
          
        });
		
		  updateDateETText();
	      updateTimeETText();
	}
   
    private void populateFields()  {
    	
  	
    	
    	// Only populate the text boxes and change the calendar date
    	// if the row is not null from the database. 
        if (mRowId != null) {
            Cursor reminder = mDbHelper.fetchTimer(mRowId);
            startManagingCursor(reminder);
            mETAddress.setText(reminder.getString(
    	            reminder.getColumnIndexOrThrow(RemindersDbAdapter.KEY_ADDRESS)));
            mETCode.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.KEY_CODE)));
            mETLocalIP.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.KEY_LOCALIP)));
            mETPort.setText(reminder.getString(
                    reminder.getColumnIndexOrThrow(RemindersDbAdapter.KEY_PORT)));
            

            // Get the date from the database and format it for our use. 
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            Date date = null;
			try {
				String dateString = reminder.getString(reminder.getColumnIndexOrThrow(RemindersDbAdapter.KEY_DATE_TIME)); 
				date = dateTimeFormat.parse(dateString);
	            mCalendar.setTime(date); 
			} catch (ParseException e) {
				Log.e("ReminderEditActivity", e.getMessage(), e); 
			} 
        } else {
        	// This is a new task - add defaults from preferences if set. 
        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
        	String defaultTitleKey = getString(R.string.pref_task_title_key);
        	String defaultTimeKey = getString(R.string.pref_default_time_from_now_key);

        	String defaultTime = prefs.getString(defaultTimeKey, null);

			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String address = sp.getString("ADDRESS", null);
			String swCode = sp.getString("SWCODE", null);
            String localip = sp.getString("LOCALIP", null);
            String port = sp.getString("PORT", null);
        	if(address != null)
        		mETAddress.setText(address);
			    mETCode.setText(swCode);
                mETLocalIP.setText(localip);
                mETPort.setText(port);
        	
        	if(defaultTime != null)
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
        outState.putLong(RemindersDbAdapter.KEY_ROWID, mRowId);
    }
    

    
    private void saveState() {
        String address = mETAddress.getText().toString();
        String code = mETCode.getText().toString();
        String localip = mETLocalIP.getText().toString();
        String port = mETPort.getText().toString();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT); 
    	String timerDateTime = dateTimeFormat.format(mCalendar.getTime());

        if (mRowId == null) {
        	
        	long id = mDbHelper.createTimer(address, code, localip, port, timerDateTime);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateTimer(mRowId, address, code, localip, port, timerDateTime);
        }
       
        new ReminderManager(this).setTimer(mRowId, mCalendar);
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
		super.onBackPressed();
		overridePendingTransition(R.anim.back2, R.anim.back1);
	}

	private void savePrefs(String key, String value) {

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	private void savePrefsBool(String key, Boolean value) {

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
    
}
