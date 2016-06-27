package com.ai.lovejoy777.ant;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by steve on 20/06/16.
 */
public class WidgetConfig extends AppCompatActivity {

    private static final String BASE_NAME = "com.ai.lovejoy777.ant.WidgetConfig";
    private static final String NODE_NAME = "com.ai.lovejoy777.ant.WidgetConfig";
    private static final String NODE_ID = "com.ai.lovejoy777.ant.WidgetConfig";
    public static final String BASE_NAME_KEY = "base_name";
    public static final String NODE_NAME_KEY = "node_name";
    public static final String NODE_ID_KEY = "node_ID";

    public final static String KEY_EXTRA_NODE_ID = "KEY_EXTRA_NODE_ID";

    Toolbar toolBar;
    BaseNodeDBHelper dbHelper;

    private ListView list;
    TextView titleTextView;
    TextView textV1;
    TextView textV2;

    private  int widgetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_configure);

        final Context context = WidgetConfig.this;

        loadToolbarNavDrawer();
        toolBar = (Toolbar) findViewById(R.id.toolbar);

        setResult(RESULT_CANCELED);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        final AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);

        textV1 = (TextView)findViewById(R.id.textV1);
        textV2 = (TextView)findViewById(R.id.textV2);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        list = (ListView) findViewById(R.id.list);
        titleTextView.setText("Pick a Node");

        dbHelper = new BaseNodeDBHelper(this);

        populateListView();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor itemCursor = (Cursor) WidgetConfig.this.list.getItemAtPosition(position);
                final int nodeID = itemCursor.getInt(itemCursor.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_ID));
                final String baseName = "" + itemCursor.getString(itemCursor.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_BASE_NAME));
                final String nodeName = "" + itemCursor.getString(itemCursor.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_NAME));

                if (!itemCursor.isClosed()) {
                    itemCursor.close();
                }
                // set widget text
                views.setTextViewText(R.id.textV1, baseName);
                views.setTextViewText(R.id.textV2, nodeName);

                String nodeid = String.valueOf(nodeID);

                saveBaseName(context, widgetID, baseName);
                saveNodeName(context, widgetID, nodeName);
                saveNodeID(context, widgetID, nodeid);


                Intent intent = new Intent(context, Node.class);
                intent.putExtra(KEY_EXTRA_NODE_ID, nodeID);
                PendingIntent pending = PendingIntent.getActivity(context, nodeID, intent, 0);
                views.setOnClickPendingIntent(R.id.imageButton1, pending);
                widgetManager.updateAppWidget(widgetID, views);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }

    // We use SharedPreferences to store the user name. The methods
    // are static so they can be called from the MainActivity too.
    static void saveBaseName(Context context, int widgetId, String baseName) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                BASE_NAME, 0).edit();
        prefs.putString(BASE_NAME_KEY + widgetId, baseName);
        prefs.commit();
    }

    static String loadBaseName(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences(BASE_NAME, 0);
        String baseName = prefs.getString(BASE_NAME_KEY + widgetId, "Default");
        return baseName;
    }

    static void saveNodeName(Context context, int widgetId, String nodeName) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                NODE_NAME, 0).edit();
        prefs.putString(NODE_NAME_KEY + widgetId, nodeName);
        prefs.commit();
    }

    static String loadNodeName(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences(NODE_NAME, 0);
        String nodeName = prefs.getString(NODE_NAME_KEY + widgetId, "Default");
        return nodeName;
    }

    static void saveNodeID(Context context, int widgetId, String nodeID) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                NODE_ID, 0).edit();
        prefs.putString(NODE_ID_KEY + widgetId, nodeID);
        prefs.commit();
    }

    static String loadNodeID(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences(NODE_ID, 0);
        String nodeID = prefs.getString(NODE_ID_KEY + widgetId, "-1");
        return nodeID;
    }


    private void populateListView() {
        final Cursor cursor = dbHelper.getAllNode();
        String[] columns = new String[]{BaseNodeDBHelper.NODE_COLUMN_BASE_NAME, BaseNodeDBHelper.NODE_COLUMN_NAME };
        int[] widgets = new int[]{R.id.text1, R.id.text2};
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.widget_info, cursor, columns, widgets, 0);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(cursorAdapter);
    }

    private void loadToolbarNavDrawer() {
        //set Toolbar
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        dbHelper.close();
        super.onBackPressed();
        overridePendingTransition(R.anim.back2, R.anim.back1);
    }
}