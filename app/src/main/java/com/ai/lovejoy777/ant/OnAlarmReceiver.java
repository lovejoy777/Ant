package com.ai.lovejoy777.ant;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class OnAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = ComponentInfo.class.getCanonicalName();

	
	
	@Override	
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received wake up from alarm manager.");
		
		long rowid = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);
		
		WakeReminderIntentService.acquireStaticLock(context);


		
		Intent i = new Intent(context, ReminderService.class); 
		i.putExtra(RemindersDbAdapter.KEY_ROWID, rowid);  
		context.startService(i);
		 
	}

}
