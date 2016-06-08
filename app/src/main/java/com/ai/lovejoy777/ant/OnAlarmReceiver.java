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
        String name = intent.getExtras().getString(TimerDbAdapter.KEY_NAME);
        String swname = intent.getExtras().getString(TimerDbAdapter.KEY_SWNAME);

        WakeTimerIntentService.acquireStaticLock(context);

        Intent i = new Intent(context, TimerService.class);
        i.putExtra(TimerDbAdapter.KEY_ROWID, rowid);
        i.putExtra(TimerDbAdapter.KEY_NAME, name);
        i.putExtra(TimerDbAdapter.KEY_SWNAME, swname);
        context.startService(i);
    }
}