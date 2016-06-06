package com.ai.lovejoy777.ant;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class ReminderService extends WakeReminderIntentService {
	Notification myNotication;
	private RemindersDbAdapter mDbHelper;
    private Long mRowId;


	public ReminderService() {
		super("ReminderService");
			}

	@Override
	void doReminderWork(Intent intent) {

        mDbHelper = new RemindersDbAdapter(this);
       // mDbHelper.open();
		Log.d("ReminderService", "Doing work.");
		mRowId = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);

		NotificationManager mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // on notification click
		Intent notificationIntent = new Intent(this, TimerLaunch.class);
		notificationIntent.putExtra(RemindersDbAdapter.KEY_ROWID, mRowId);

        // Instant launch
        Intent i = new Intent(this, TimerLaunch.class);
        i.putExtra(RemindersDbAdapter.KEY_ROWID, mRowId);
        startActivity(i);

		Notification.Builder builder = new Notification.Builder(ReminderService.this);


		builder.setAutoCancel(true);
		builder.setTicker("Ant Timer Activated !!");
		builder.setContentTitle("Timer Activated!!");
		builder.setContentText("Timer id:" + mRowId);
		builder.setSmallIcon(R.mipmap.ic_launcher);
		//builder.setContentIntent(pi);
		builder.setOngoing(false);
		builder.setNumber(100);
		builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
		builder.setVibrate(new long[] { 1000, 1000});
		builder.build();

		myNotication = builder.getNotification();

		
		// An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value). 
		// I highly doubt this will ever happen. But is good to note. 

        int id = (int)((long)mRowId);
		mgr.notify(id, myNotication);

		
	}

}
