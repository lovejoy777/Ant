package com.ai.lovejoy777.ant;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ReminderManager {

	private Context mContext; 
	private AlarmManager mAlarmManager;
	
	public ReminderManager(Context context) {
		mContext = context; 
		mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}
	
	public void setTimer(Long taskId, Calendar when) {


        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.putExtra(RemindersDbAdapter.KEY_ROWID, (long)taskId);
        long tid = taskId;
        int taskid = (int)tid;

        PendingIntent pi = PendingIntent.getBroadcast(mContext, taskid, i,0);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
	}

    public void deleteTimer(Long taskId) {

        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.putExtra(RemindersDbAdapter.KEY_ROWID, (long)taskId);
        long tid = taskId;
        int taskid = (int)tid;

        PendingIntent pi = PendingIntent.getBroadcast(mContext, taskid, i,0);

        mAlarmManager.cancel(pi);
    }
}
