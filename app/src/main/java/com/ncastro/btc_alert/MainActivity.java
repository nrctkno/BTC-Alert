package com.ncastro.btc_alert;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private TextView lblLast;
    private TextView lblLastTime;

    private Integer val_low;
    private Integer val_high;

    private Integer val_low_min;
    private Integer val_high_max;

    private Settings settings;
    private BTCServerPeriodicQuery periodic_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        settings = new Settings(this) {
            @Override
            public void onLoadValues(Integer val_low, Integer val_high) {
                onValuesLoaded(val_low, val_high);
            }
        };

        settings.loadValues();

        lblLast = (TextView) findViewById(R.id.lblLast);
        lblLastTime = (TextView) findViewById(R.id.lblLastTime);

        periodic_query = new BTCServerPeriodicQuery(this, 5000, "USD") {
            @Override
            public void onNewPrice(Float price) {
                showPrice(price);
            }

            @Override
            public void onError(String message) {
                lblLast.setText("-");
                Utils.showToast(getApplicationContext(), message);
            }
        };

        setupFields();

        Notifications.createNotificationChannel(this);
        //launchBackgroundService();
        BackgroundJobScheduler.schedule(getApplicationContext());
    }

    public void setupFields() {
        Spinner currencies = (Spinner) findViewById(R.id.inputCurrency);

        String[] items = new String[]{"USD", "EUR", "GPB"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        currencies.setAdapter(adapter);
    }

    public void onValuesLoaded(Integer val_low, Integer val_high) {
        this.val_low = val_low;
        this.val_high = val_high;

        val_low_min = val_low;
        val_high_max = val_high;

        EditText inputLow = (EditText) findViewById(R.id.inputLow);
        EditText inputHigh = (EditText) findViewById(R.id.inputHigh);

        inputLow.setText(val_low.toString());
        inputHigh.setText(val_high.toString());
    }


    /**
     * Called when the user taps the Send button
     */
    public void onSaveValuesButtonClicked(View view) {

        EditText inputLow = (EditText) findViewById(R.id.inputLow);
        EditText inputHigh = (EditText) findViewById(R.id.inputHigh);

        val_low = Integer.parseInt(inputLow.getText().toString());
        val_high = Integer.parseInt(inputHigh.getText().toString());

        val_low_min = val_low;
        val_high_max = val_high;

        settings.save(val_low, val_high);

        Utils.showToast(getApplicationContext(), "Guardado");
    }

    public void onShowOptionsButtonClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        /*
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
         */
        startActivity(intent);


    }

    public void showPrice(Float price) {
        String price_str = String.format("%.2f", price);
        String time_str = Utils.getTime();

        lblLast.setText(price_str);
        lblLastTime.setText("Actualizado a las " + Utils.getTime());

        if (price < val_low) {
            lblLast.setTextColor(Color.RED);
        } else if (price > val_high) {
            lblLast.setTextColor(Color.GREEN);
        } else {
            lblLast.setTextColor(Color.CYAN);
        }
    }


}
