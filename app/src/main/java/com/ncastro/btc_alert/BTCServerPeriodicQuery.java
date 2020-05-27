package com.ncastro.btc_alert;


import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

public class BTCServerPeriodicQuery {

    private BTCServer btcserver;
    private String currency;

    public BTCServerPeriodicQuery(Context context, int period, String _currency) {

        currency = _currency;

        btcserver = new BTCServer(context) {
            @Override
            public void newPrice(Float price) {
                onNewPrice(price);
            }

            @Override
            public void error(String message) {
                onError(message);
            }
        };


        Timer timer = new Timer(true);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                btcserver.getPrice(currency);
            }
        };

        timer.scheduleAtFixedRate(task, 0, period);
    }

    public void onNewPrice(Float price) {

    }

    public void onError(String name) {

    }

}
