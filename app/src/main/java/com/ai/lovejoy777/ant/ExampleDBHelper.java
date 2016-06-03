package com.ai.lovejoy777.ant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by obaro on 02/04/2015.
 */
public class ExampleDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteExample.db";
    private static final int DATABASE_VERSION = 3;

    public static final String NODE_TABLE_NAME = "Nodes";
    public static final String NODE_COLUMN_ID = "_id";
    public static final String NODE_COLUMN_NAME = "name";
    public static final String NODE_COLUMN_ADDRESS = "address";
    public static final String NODE_COLUMN_RSADDRESS = "rsaddress";
    public static final String NODE_COLUMN_TYPE = "type";
    public static final String NODE_COLUMN_SWNUM = "swnum";
    public static final String NODE_COLUMN_SW1 = "sw1";
    public static final String NODE_COLUMN_SW2 = "sw2";
    public static final String NODE_COLUMN_SW3 = "sw3";
    public static final String NODE_COLUMN_SW4 = "sw4";
    public static final String NODE_COLUMN_BASE_ID = "base_id";

    public static final String BASE_TABLE_NAME = "Bases";
    public static final String BASE_COLUMN_ID = "_id";
    public static final String BASE_COLUMN_NAME = "name";
    public static final String BASE_COLUMN_LOCALIP = "localip";
    public static final String BASE_COLUMN_PORT = "port";

    public ExampleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Nodes Table
        db.execSQL(
                "CREATE TABLE " + NODE_TABLE_NAME +
                        "(" + NODE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        NODE_COLUMN_NAME + " TEXT, " +
                        NODE_COLUMN_ADDRESS + " TEXT, " +
                        NODE_COLUMN_RSADDRESS + " TEXT, " +
                        NODE_COLUMN_TYPE + " TEXT, " +
                        NODE_COLUMN_SWNUM + " TEXT, " +
                        NODE_COLUMN_SW1 + " TEXT, " +
                        NODE_COLUMN_SW2 + " TEXT, " +
                        NODE_COLUMN_SW3 + " TEXT, " +
                        NODE_COLUMN_SW4 + " TEXT, " +
                        NODE_COLUMN_BASE_ID + " INTEGER)"
        );

        // Create Base Table
        db.execSQL(
                "CREATE TABLE " + BASE_TABLE_NAME +
                        "(" + BASE_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        BASE_COLUMN_NAME + " TEXT, " +
                        BASE_COLUMN_LOCALIP + " TEXT, " +
                        BASE_COLUMN_PORT + " TEXT)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NODE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BASE_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNode(String name, String address, String rsaddress, String type, String swnum, String sw1, String sw2, String sw3, String sw4, String base_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NODE_COLUMN_NAME, name);
        contentValues.put(NODE_COLUMN_ADDRESS, address);
        contentValues.put(NODE_COLUMN_RSADDRESS, rsaddress);
        contentValues.put(NODE_COLUMN_TYPE, type);
        contentValues.put(NODE_COLUMN_SWNUM, swnum);
        contentValues.put(NODE_COLUMN_SW1, sw1);
        contentValues.put(NODE_COLUMN_SW2, sw2);
        contentValues.put(NODE_COLUMN_SW3, sw3);
        contentValues.put(NODE_COLUMN_SW4, sw4);
        contentValues.put(NODE_COLUMN_BASE_ID, base_id);

        db.insert(NODE_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertBase(String name, String localip, String port) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BASE_COLUMN_NAME, name);
        contentValues.put(BASE_COLUMN_LOCALIP, localip);
        contentValues.put(BASE_COLUMN_PORT, port);

        db.insert(BASE_TABLE_NAME, null, contentValues);
        return true;
    }


    public int numberOfNodeRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, NODE_TABLE_NAME);
        return numRows;
    }

    public int numberOfBaseRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, BASE_TABLE_NAME);
        return numRows;
    }

    public boolean updateNode(Integer id, String name, String address, String rsaddress, String type, String swnum, String sw1, String sw2, String sw3, String sw4, String base_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NODE_COLUMN_NAME, name);
        contentValues.put(NODE_COLUMN_ADDRESS, address);
        contentValues.put(NODE_COLUMN_RSADDRESS, rsaddress);
        contentValues.put(NODE_COLUMN_TYPE, type);
        contentValues.put(NODE_COLUMN_SWNUM, swnum);
        contentValues.put(NODE_COLUMN_SW1, sw1);
        contentValues.put(NODE_COLUMN_SW2, sw2);
        contentValues.put(NODE_COLUMN_SW3, sw3);
        contentValues.put(NODE_COLUMN_SW4, sw4);
        contentValues.put(NODE_COLUMN_BASE_ID, base_id);
        db.update(NODE_TABLE_NAME, contentValues, NODE_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateBase(Integer id, String name, String localip, String port) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BASE_COLUMN_NAME, name);
        contentValues.put(BASE_COLUMN_LOCALIP, localip);
        contentValues.put(BASE_COLUMN_PORT, port);
        db.update(BASE_TABLE_NAME, contentValues, BASE_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteNode(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NODE_TABLE_NAME,
                NODE_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteBase(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(BASE_TABLE_NAME,
                BASE_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteAllBaseEntries(Integer id) {
      //  new String query = "Select * FROM " + ENTRY_TABLE_NAME + " WHERE " + ENTRY_COLUMN_TRIP_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NODE_TABLE_NAME,
                 NODE_COLUMN_BASE_ID + "=?", new String[] { Integer.toString(id) });
    }

    public Cursor getNode(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + NODE_TABLE_NAME + " WHERE " +
                NODE_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getBase(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + BASE_TABLE_NAME + " WHERE " +
                BASE_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllNode() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + NODE_TABLE_NAME, null );
        return res;
    }

    public Cursor getAllBase() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + BASE_TABLE_NAME, null );
        return res;
    }

    public Cursor getBaseNode(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + NODE_TABLE_NAME + " WHERE " +
                NODE_COLUMN_BASE_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }
}