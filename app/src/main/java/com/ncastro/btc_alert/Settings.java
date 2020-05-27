package com.ncastro.btc_alert;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private Context context;

    public Settings(Context context) {
        this.context = context;
    }

    public SharedPreferences getPreferences() {
        return context.getSharedPreferences("values",Context.MODE_PRIVATE); //getPreferences();
    }
    public void loadValues() {
        SharedPreferences sharedPref = getPreferences();

        Integer val_low = sharedPref.getInt("value_low", 0);
        Integer val_high = sharedPref.getInt("value_high", 10000);

        onLoadValues(val_low, val_high);
    }

    public void onLoadValues(Integer val_low,Integer  val_high){
    }

    public void save(Integer val_low, Integer val_high) {
        SharedPreferences sharedPref =  getPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("value_low", val_low);
        editor.putInt("value_high", val_high);
        editor.commit();
    }

}
