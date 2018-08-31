package com.example.dell.student;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dell.student.constants.JApplication;
import com.example.dell.student.constants.VolleySingleton;
import com.example.dell.student.room.AppDatabase;
import com.example.dell.student.room.CS_Doc;
import com.example.dell.student.room.CS_Subjects;
import com.example.dell.student.room.IT_Doc;
import com.example.dell.student.room.IT_Subjects;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyIntentService extends IntentService {

    public static final String ACTION = "dell.example.com.myservice.MyIntentService";
    String msg, course;
    String timestamp;
    AppDatabase appDatabase;
    StringRequest getLatestRows = new StringRequest(Request.Method.POST, JApplication.getGetLatestRows(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("tagg", "MyIntentService getLatestRows response" + response);
                    Gson gson = new Gson();
                    if (course.equals("Information Technology")) {
                        IT_Doc[] it_docs = gson.fromJson(response, IT_Doc[].class);
                        appDatabase.subjectsDao().insertITDocs(it_docs);
                    } else if (course.equals(("Computer Science"))) {
                        Log.i("tagg", "MyIntentService getLatestRows inside " + course);

                        CS_Doc[] cs_docs = gson.fromJson(response, CS_Doc[].class);
//                        Log.i("tagg","MyIntentService getLatestRows cs_docs "+ cs_docs[cs_docs.length-1].getDocument_name());

                        appDatabase.subjectsDao().insertCSDocs(cs_docs);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i("tagg", "MyIntentService getLatestRows response" + error.toString());
                    Toast.makeText(MyIntentService.this, error.toString(), Toast.LENGTH_LONG).show();

                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> prams = new HashMap<>();
            prams.put("course", course);
            Log.i("tagg", "timestamp2 = " + timestamp);
            prams.put("timestamp", timestamp);
            return prams;
        }
    };
    StringRequest checkForUpdate = new StringRequest(Request.Method.POST, JApplication.getCheckForUpdate(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("tagg", "MyIntentService checkForUpdate response here");
                    Log.i("tagg", "MyIntentService checkForUpdate response" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("response");
                        if (result.equals("upToDate")) {
                            Log.i("tagg", "UpToDate");
                        } else if (result.equals("Not Updated")) {
                            getLatestRows.setShouldCache(false);
                            VolleySingleton.getInstance(getApplicationContext()).addToRequestque(getLatestRows);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i("tagg", "MyIntentService checkForUpdate error " + error.getMessage());
                    //Toast.makeText(MyService.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> prams = new HashMap<>();
            prams.put("course", course);
            Log.i("tagg", "timestamp1 = " + timestamp);
            prams.put("timestamp", timestamp);
            return prams;
        }
    };
    StringRequest itStringRequest = new StringRequest(Request.Method.POST, JApplication.getSubjectTables(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.i("tagg","MyIntentService itStringRequest " + response);
                    Gson gson = new Gson();
                    IT_Subjects[] itSubjects = gson.fromJson(response, IT_Subjects[].class);
                    appDatabase.subjectsDao().insertITSubjects(itSubjects);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    //Log.i("tagg","MyIntentService itStringRequest error"+error.toString());
                    Toast.makeText(MyIntentService.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> prams = new HashMap<>();
            prams.put("course", "IT");
            return prams;
        }
    };
    StringRequest csStringRequest = new StringRequest(Request.Method.POST, JApplication.getSubjectTables(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("tagg", "MyIntentService csStringRequest " + response);
                    Gson gson = new Gson();
                    CS_Subjects[] csSubjects = gson.fromJson(response, CS_Subjects[].class);
                    appDatabase.subjectsDao().insertCSSubjects(csSubjects);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i("tagg", "MyIntentService csStringRequest error" + error.toString());
                    Toast.makeText(MyIntentService.this, "MyIntentService " + error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> prams = new HashMap<>();
            prams.put("course", "CS");
            return prams;
        }
    };

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        Log.i("tagg", "MyIntentService onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.i("tagg", "MyIntentService onStartCommand");
        appDatabase = AppDatabase.getAppDatabase(MyIntentService.this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        msg = intent.getStringExtra("msg");
        course = intent.getStringExtra("course");
        Log.i("tagg", "MyIntentService msg = " + msg);
        if (msg.equals("getSubject")) {
            downloadSubjects(course);
        } else if (msg.equals("checkForUpdates")) {
            checkForUpdates(course);
        }

    }

    @Override
    public void onDestroy() {
        Log.i("tagg", "MyIntentService onDestroy");
        //AppDatabase.destroyInstance();
        super.onDestroy();
    }

    private void downloadSubjects(String course) {
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();
        if (course.equals("Information Technology")) {
            itStringRequest.setShouldCache(false);
            VolleySingleton.getInstance(getApplicationContext()).addToRequestque(itStringRequest);
            appDatabase.subjectsDao().insertITSubjects();
        } else if (course.equals("Computer Science")) {
            csStringRequest.setShouldCache(false);
            VolleySingleton.getInstance(getApplicationContext()).addToRequestque(csStringRequest);
            appDatabase.subjectsDao().insertCSSubjects();
        }
    }

    private void checkForUpdates(String course) {
        Log.i("tagg", "MyIntentService checkForUpdates " + course);
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();
        String temp = null;

        checkForUpdate.setShouldCache(false);

        if (course.equals("Information Technology")) {
            temp = appDatabase.subjectsDao().getITTimestamp();
        } else if (course.equals("Computer Science")) {
            temp = appDatabase.subjectsDao().getCSTimestamp();
            Log.i("tagg", "MyIntentService checkForUpdates HERE HERE " + String.valueOf(temp));
        }
        if (String.valueOf(temp).equals(String.valueOf(timestamp))) {
            timestamp = "";
        } else {
            timestamp = temp;
        }
        Log.i("tagg", "MyIntentService checkForUpdates HERE HERE1");
        Log.i("tagg", "timestamp0 = " + timestamp);
        Log.i("tagg", "checkForUpdate BEFORE ");
        VolleySingleton.getInstance(getApplicationContext()).addToRequestque(checkForUpdate);
        Log.i("tagg", "checkForUpdate AFTER ");
        Toast.makeText(getApplicationContext(), "service runninggg", Toast.LENGTH_SHORT).show();
    }

}
