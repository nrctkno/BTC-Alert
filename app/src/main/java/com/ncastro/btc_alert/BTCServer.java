package com.ncastro.btc_alert;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class BTCServer {

    private String BASE_URL = "https://cex.io/api/last_price/BTC/";
    private RequestQueue queue;

    public BTCServer(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void getPrice(String currency) {
        // Instantiate the RequestQueue.
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, BASE_URL + currency, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Float price = Float.parseFloat(response.getString("lprice"));
                            newPrice(price);

                        } catch (JSONException e) {
                            error(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError e) {
                        error(e.getMessage());
                    }
                });

        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        queue.add(request);
    }

    public void newPrice(Float price) {

    }

    public void error(String message) {

    }

}
