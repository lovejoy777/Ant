
package com.ai.lovejoy777.ant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple timer database access helper class.
 * Defines the basic CRUD operations (Create, Read, Update, Delete)
 * for the example, and gives the ability to list all timers as well as
 * retrieve or modify a specific timer.
 */
public class TimerDbAdapter {

    //
    // Databsae Related Constants
    //
    private static final String DATABASE_NAME = "TimerData";
    private static final String DATABASE_TABLE = "Timers";
    private static final int DATABASE_VERSION = 7;

    public static final String KEY_NAME = "name";
    public static final String KEY_SWNAME = "swname";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CODE = "code";
    public static final String KEY_LOCALIP = "localip";
    public static final String KEY_PORT = "port";
    public static final String KEY_DATE_TIME = "timer_date_time";
    public static final String KEY_REPEAT = "repeat";
    public static final String KEY_ROWID = "_id";


    private static final String TAG = "TimerDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation SQL statement
     */
    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_NAME + " text not null, "
                    + KEY_SWNAME + " text not null, "
                    + KEY_ADDRESS + " text not null, "
                    + KEY_CODE + " text not null, "
                    + KEY_LOCALIP + " text not null, "
                    + KEY_PORT + " text not null, "
                    + KEY_DATE_TIME + " text not null, "
                    + KEY_REPEAT + " text not null);";


    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public TimerDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     * initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public TimerDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new timer using the name, swname, address, code, localip, port, timer date time provided and swname.
     * If the timer is  successfully created return the new rowId
     * for that timer, otherwise return a -1 to indicate failure.
     *
     * @param name          the name of the timer
     * @param swname          the name of the timer
     * @param address       the address of the timer
     * @param code          the switch code of the timer
     * @param localip       the local ip of the timer
     * @param port          the port of the timer
     * @param timerDateTime date and time the timer should remind the user
     * @param repeat        the repeat of the timer
     * @return rowId or -1 if failed
     */
    public long createTimer(String name, String swname, String address, String code, String localip, String port, String timerDateTime, String repeat) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_SWNAME, swname);
        initialValues.put(KEY_ADDRESS, address);
        initialValues.put(KEY_CODE, code);
        initialValues.put(KEY_LOCALIP, localip);
        initialValues.put(KEY_PORT, port);
        initialValues.put(KEY_DATE_TIME, timerDateTime);
        initialValues.put(KEY_REPEAT, repeat);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the timer with the given rowId
     *
     * @param rowId id of timer to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteTimer(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Delete all timers from rows
     */
    public void deleteAll() {

        mDb.delete(DATABASE_TABLE, null, null);
    }

    /**
     * Return a Cursor over the list of all timers in the database
     *
     * @return Cursor over all timers
     */
    public Cursor fetchAllTimers() {

        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_SWNAME, KEY_ADDRESS,
                KEY_CODE, KEY_LOCALIP, KEY_PORT, KEY_DATE_TIME, KEY_REPEAT}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the timer that matches the given rowId
     *
     * @param rowId id of timer to retrieve
     * @return Cursor positioned to matching timer, if found
     * @throws SQLException if timer could not be found/retrieved
     */
    public Cursor fetchTimer(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,
                                KEY_ADDRESS, KEY_NAME, KEY_SWNAME, KEY_CODE, KEY_LOCALIP, KEY_PORT, KEY_DATE_TIME, KEY_REPEAT}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the timer using the details provided. The timer to be updated is
     * specified using the rowId, and it is altered to use the name, address, code, localip, port, date time, and repeat
     * values passed in
     *
     * @param rowId         id of timer to update
     * @param name          value to set timer name to
     * @param swname          value to set timer swname to
     * @param address       value to set timer address to
     * @param code          value to set timer code to
     * @param localip       value to set timer localip to
     * @param port          value to set timer port to
     * @param timerDateTime value to set the timer time
     * @param repeat          value to set timer repeat to
     * @return true if the timer was successfully updated, false otherwise
     */
    public boolean updateTimer(long rowId, String name, String swname, String address, String code, String localip, String port, String timerDateTime, String repeat) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_SWNAME, swname);
        args.put(KEY_ADDRESS, address);
        args.put(KEY_CODE, code);
        args.put(KEY_LOCALIP, localip);
        args.put(KEY_PORT, port);
        args.put(KEY_DATE_TIME, timerDateTime);
        args.put(KEY_REPEAT, repeat);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}