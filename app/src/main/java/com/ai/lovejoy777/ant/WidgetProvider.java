package com.ai.lovejoy777.ant;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by steve on 19/06/16.
 */
public class WidgetProvider extends AppWidgetProvider{

    public final static String KEY_EXTRA_NODE_ID = "KEY_EXTRA_NODE_ID";

    @Override
    public void  onUpdate(Context context, AppWidgetManager appWidgetManager,
                          int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for(int i=0; i<appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, Node.class);
            PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
            views.setOnClickPendingIntent(R.id.imageButton1, pending);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }
}
