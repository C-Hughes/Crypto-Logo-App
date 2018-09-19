package com.example.camnc.cryptologogame.Widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;

import com.example.camnc.cryptologogame.MainActivity;
import com.example.camnc.cryptologogame.R;
import com.example.camnc.cryptologogame.Widget.CryptoPriceWidgetConfigureActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CryptoPriceWidgetConfigureActivity CryptoPriceWidgetConfigureActivity}
 */
//button for android widget
//https://stackoverflow.com/questions/14798073/button-click-event-for-android-widget

public class CryptoPriceWidget extends AppWidgetProvider {
    private static final String SYNC_CLICKED = "automaticWidgetSyncButtonClick";
    private RemoteViews remoteViews;
    private ComponentName watchWidget;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        new JsonTask(context).execute("https://api.coinmarketcap.com/v1/ticker/bitcoin/?convert=GBP");

        // Set listener for refresh button.
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.crypto_price_widget);
        watchWidget = new ComponentName(context, CryptoPriceWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.btnRefresh, getPendingSelfIntent(context, SYNC_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            CryptoPriceWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        //When button is clicked, update price.
        if (SYNC_CLICKED.equals(intent.getAction())) {
            new JsonTask(context).execute("https://api.coinmarketcap.com/v1/ticker/bitcoin/?convert=GBP");
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private class JsonTask extends AsyncTask<String, String, String> {
        private Context CONTEXT;

        public JsonTask(Context context){
            this.CONTEXT = context;
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();

            } catch (MalformedURLException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}
            finally {
                if (connection != null) {connection.disconnect();}
                try {
                    if (reader != null) {reader.close();}
                }
                catch (IOException e) {e.printStackTrace();}
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Cut all JSON data except GBP price
            String GBPPrice = result.substring(result.indexOf("price_gbp") + 13, result.indexOf("price_gbp") + 20);
            result = "BTC: £"+GBPPrice;

            //Update Widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(CONTEXT);

            remoteViews = new RemoteViews(CONTEXT.getPackageName(), R.layout.crypto_price_widget);
            watchWidget = new ComponentName(CONTEXT, CryptoPriceWidget.class);

            remoteViews.setTextViewText(R.id.appwidget_text, result);

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

