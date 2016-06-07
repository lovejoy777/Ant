package com.ai.lovejoy777.ant;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class TimerManager {

    private Context mContext;
    private AlarmManager mAlarmManager;

    public TimerManager(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setTimer(Long taskId, Calendar when, String repeat) {

        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.putExtra(TimerDbAdapter.KEY_ROWID, (long) taskId);
        long tid = taskId;
        int taskid = (int) tid;

        PendingIntent pi = PendingIntent.getBroadcast(mContext, taskid, i, 0);

        if (repeat != null) {
            if (repeat.equals("Day")) {
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), (AlarmManager.INTERVAL_DAY), pi);
                //(24 * 1000 * 60 * 60) = daily
            }
            if (repeat.equals("Week")) {
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), (AlarmManager.INTERVAL_DAY * 7), pi);
                //(24 * 1000 * 60 * 60 * 7) = weekly
            }
            if (repeat.equals("Not Set")) {
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
                // one off
            }
        } else {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
            // one off
        }
    }

    public void deleteTimer(Long taskId) {

        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.putExtra(TimerDbAdapter.KEY_ROWID, (long) taskId);
        long tid = taskId;
        int taskid = (int) tid;

        PendingIntent pi = PendingIntent.getBroadcast(mContext, taskid, i, 0);

        mAlarmManager.cancel(pi);
    }
}