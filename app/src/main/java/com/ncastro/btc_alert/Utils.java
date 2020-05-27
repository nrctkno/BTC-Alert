package com.ncastro.btc_alert;

import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;

public class Utils {

    public static String getTime() {
        Calendar calendar = Calendar.getInstance();

        return          String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))
                + ":" + String.format("%02d", calendar.get(Calendar.MINUTE))
                + ":" + String.format("%02d", calendar.get(Calendar.SECOND));
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
