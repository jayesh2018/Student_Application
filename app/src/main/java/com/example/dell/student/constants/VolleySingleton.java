package com.example.dell.student.constants;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton mySingleton;
    private static Context context;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context) {
        this.context = context;
        getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {

        if (mySingleton == null) {
            mySingleton = new VolleySingleton(context);
        }
        return mySingleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {

            requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        }
        return requestQueue;
    }

    public <T> void addToRequestque(Request<T> request) {
        requestQueue.add(request);
    }


}
