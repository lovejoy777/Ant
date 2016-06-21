package com.ai.lovejoy777.ant;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
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

    public final static String KEY_EXTRA_NODE_ID = "KEY_EXTRA_NODE_ID";

    Toolbar toolBar;

    BaseNodeDBHelper dbHelper;

    private ListView list;

    TextView titleTextView;
    TextView textV1;
    TextView textV2;

    private WidgetConfig context;
    private  int widgetID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_configure);

        loadToolbarNavDrawer();
        toolBar = (Toolbar) findViewById(R.id.toolbar);

        setResult(RESULT_CANCELED);
        context = this;

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
                int nodeID = itemCursor.getInt(itemCursor.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_ID));
                String baseName = "" + itemCursor.getString(itemCursor.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_BASE_NAME));
                String nodeName = "" + itemCursor.getString(itemCursor.getColumnIndex(BaseNodeDBHelper.NODE_COLUMN_NAME));

                if (!itemCursor.isClosed()) {
                    itemCursor.close();
                }
                // set widget text
                views.setTextViewText(R.id.textV1, baseName);
                views.setTextViewText(R.id.textV2, nodeName);

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