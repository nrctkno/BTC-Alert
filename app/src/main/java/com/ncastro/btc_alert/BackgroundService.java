package com.ncastro.btc_alert;

import android.app.Activity;
import android.app.IntentService;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Date;


public class BackgroundService extends IntentService {

    private BTCServer btcserver;

    private Integer val_low;
    private Integer val_high;

    private Settings settings;

    public BackgroundService() {
        super("btc_alert_background_service");
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.

        settings = new Settings(this) {
            @Override
            public void onLoadValues(Integer val_low, Integer val_high) {
                onValuesLoaded(val_low, val_high);
            }
        };

        settings.loadValues();

        btcserver = new BTCServer(this) {
            @Override
            public void newPrice(Float price) {
                updateNotifications(price);
                updateWidgets(price);
            }

            @Override
            public void error(String message) {
                Utils.showToast(getApplicationContext(), message);
            }
        };

        Notifications.createNotificationChannel(this);
        Log.i("btc background service", "created");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("btc background service", "executing");
        btcserver.getPrice("USD");
    }

    public void onValuesLoaded(Integer _val_low, Integer _val_high) {
        val_low = _val_low;
        val_high = _val_high;
    }

    public void updateNotifications(Float price) {
        String price_str = String.format("%.2f", price);
        String time_str = Utils.getTime();

        if (price < val_low) {
            Notifications.notify(this, 1, "Bitcoin a " + price_str, "Nuevo mínimo a las " + time_str, R.color.color_red);
        }

        if (price > val_high) {
            Notifications.notify(this, 2, "Bitcoin a " + price_str, "Nuevo máximo a las " + time_str, R.color.color_green);
        }
    }

    public void updateWidgets(Float price) {
        String price_str = String.format("%.2f", price);
        String time_str = Utils.getTime();

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.currency_widget_activity);
        view.setTextViewText(R.id.lblValue, price_str);
        view.setTextViewText(R.id.lblLastUpdate, "BTC / USD  " + time_str);

        if (price < val_low) {
            view.setTextColor(R.id.cw_lblMinMax, Color.RED);
        } else if (price > val_high) {
            view.setTextColor(R.id.cw_lblMinMax, Color.GREEN);
        } else {
            view.setTextColor(R.id.cw_lblMinMax, Color.CYAN);
        }

        ComponentName theWidget = new ComponentName(this, CurrencyWidgetActivity.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, view);
    }

}