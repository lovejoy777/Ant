package com.ai.lovejoy777.ant;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.util.Log;

public class OnAlarmReceiver extends BroadcastReceiver {

    private static final String TAG = ComponentInfo.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received wake up from alarm manager.");

        long rowid = intent.getExtras().getLong(TimerDbAdapter.KEY_ROWID);

        WakeTimerIntentService.acquireStaticLock(context);

        Intent i = new Intent(context, TimerService.class);
        i.putExtra(TimerDbAdapter.KEY_ROWID, rowid);
        context.startService(i);
    }
}