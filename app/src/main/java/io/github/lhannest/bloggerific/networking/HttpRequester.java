package io.github.lhannest.bloggerific.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by root on 05/03/17.
 */

public class HttpRequester {

    public static void makeRequest(Context context, int requestMethod, String url, Response.Listener<String> responseListener) {
        makeRequest(context, requestMethod, url, responseListener, null);
    }

    public static void makeRequest(Context context, int requestMethod, String url, Response.Listener<String> responseListener, final String jsonContent) {
        RequestQueue queue = Volley.newRequestQueue(context);

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO: Implement eventually!! Will probably need to re-request the oauth2token
                Log.i(">>>>>", "HttpRequester.makeRequest ErrorListener is active");
            }
        };

        StringRequest stringRequest;

        if (jsonContent == null) {
            stringRequest = new StringRequest(requestMethod, url, responseListener, errorListener);
        } else {
            stringRequest = new StringRequest(requestMethod, url, responseListener, errorListener) {

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return jsonContent.getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

            };
        }

        queue.add(stringRequest);
    }
}
