package com.ai.lovejoy777.ant;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class TimerService extends WakeTimerIntentService {
    Notification myNotication;
    private TimerDbAdapter mDbHelper;
    private Long mRowId;

    public TimerService() {
        super("TimerService");
    }

    @Override
    void doReminderWork(Intent intent) {

        mDbHelper = new TimerDbAdapter(this);
        // mDbHelper.open();
        Log.d("TimerService", "Doing work.");
        mRowId = intent.getExtras().getLong(TimerDbAdapter.KEY_ROWID);

        NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // on notification click
        Intent notificationIntent = new Intent(this, TimerLaunch.class);
        notificationIntent.putExtra(TimerDbAdapter.KEY_ROWID, mRowId);

        // Instant launch
        Intent i = new Intent(this, TimerLaunch.class);
        i.putExtra(TimerDbAdapter.KEY_ROWID, mRowId);
        startActivity(i);

        Notification.Builder builder = new Notification.Builder(TimerService.this);

        builder.setAutoCancel(true);
        builder.setTicker("Ant Timer Activated !!");
        builder.setContentTitle("Timer Activated!!");
        builder.setContentText("Timer id:" + mRowId);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //builder.setContentIntent(pi);
        builder.setOngoing(false);
        builder.setNumber(100);
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        long[] vibrate = { 500, 1000};
        builder.setVibrate(vibrate);
        builder.build();

        myNotication = builder.getNotification();

        // An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value).
        // I highly doubt this will ever happen. But is good to note.
        int id = (int) ((long) mRowId);
        mgr.notify(id, myNotication);

    }
}