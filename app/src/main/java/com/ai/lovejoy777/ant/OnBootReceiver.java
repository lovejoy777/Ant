package com.ai.lovejoy777.ant;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.database.Cursor;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {

    private static final String TAG = ComponentInfo.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {

        TimerManager reminderMgr = new TimerManager(context);

        TimerDbAdapter dbHelper = new TimerDbAdapter(context);
        dbHelper.open();

        Cursor cursor = dbHelper.fetchAllTimers();

        if (cursor != null) {
            cursor.moveToFirst();

            int rowIdColumnIndex = cursor.getColumnIndex(TimerDbAdapter.KEY_ROWID);
            int dateTimeColumnIndex = cursor.getColumnIndex(TimerDbAdapter.KEY_DATE_TIME);
            int repeatColumnIndex = cursor.getColumnIndex(TimerDbAdapter.KEY_REPEAT);

            while (cursor.isAfterLast() == false) {

                Log.d(TAG, "Adding alarm from boot.");
                Log.d(TAG, "Row Id Column Index - " + rowIdColumnIndex);
                Log.d(TAG, "Date Time Column Index - " + dateTimeColumnIndex);
                Log.d(TAG, "Repeat Column Index - " + repeatColumnIndex);

                Long rowId = cursor.getLong(rowIdColumnIndex);
                String dateTime = cursor.getString(dateTimeColumnIndex);
                String repeat = cursor.getString(repeatColumnIndex);

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat(TimerEditActivity.DATE_TIME_FORMAT);

                try {
                    java.util.Date date = format.parse(dateTime);
                    cal.setTime(date);

                    reminderMgr.setTimer(rowId, cal, repeat);
                } catch (java.text.ParseException e) {
                    Log.e("OnBootReceiver", e.getMessage(), e);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        dbHelper.close();
    }
}