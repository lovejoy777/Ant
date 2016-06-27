package com.ai.lovejoy777.ant;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by steve on 19/06/16.
 */
public class WidgetProvider extends AppWidgetProvider {

    public final static String KEY_EXTRA_NODE_ID = "KEY_EXTRA_NODE_ID";
    private static final String LOG_TAG = "CW_EX";
    TextView textV1;
    TextView textV2;


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "onReceive");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] widgetIds) {
        super.onUpdate(context, appWidgetManager, widgetIds);
        for (int i = 0; i < widgetIds.length; i++) {
            int widgetId = widgetIds[i];


            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);

            int nodeID = -1;
            String nodeId = WidgetConfig.loadNodeID(context, widgetId);
            nodeID = Integer.parseInt(nodeId);

            Intent intent = new Intent(context, Node.class);
            intent.putExtra(KEY_EXTRA_NODE_ID, nodeID);
            PendingIntent pending = PendingIntent.getActivity(context, nodeID, intent, 0);

            views.setOnClickPendingIntent(R.id.imageButton1, pending);

            String baseName = WidgetConfig.loadBaseName(context, widgetId);
            if (baseName != null) {
                views.setTextViewText(R.id.textV1, baseName);
            }

            String nodeName = WidgetConfig.loadNodeName(context, widgetId);
            if (nodeName != null) {
                views.setTextViewText(R.id.textV2, nodeName);
            }

            appWidgetManager.updateAppWidget(widgetId, views);

        }
    }
}