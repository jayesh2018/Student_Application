package com.example.dell.student;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
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

public class MyService extends Service {

    String msg, course;
    String timestamp = new String();
    AppDatabase appDatabase;
    StringRequest getLatestRows = new StringRequest(Request.Method.POST, JApplication.getGetLatestRows(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("inferno", "MyService getLatestRows response" + response);
                    Gson gson = new Gson();
                    if (course.equals("Information Technology")) {
                        IT_Doc[] it_docs = gson.fromJson(response, IT_Doc[].class);
                        appDatabase.subjectsDao().insertITDocs(it_docs);
                    } else if (course.equals(("Computer Science"))) {
                        Log.i("inferno", "MyService getLatestRows inside " + course);

                        CS_Doc[] cs_docs = gson.fromJson(response, CS_Doc[].class);
                        //Log.i("inferno","MyService getLatestRows cs_docs "+ cs_docs[cs_docs.length-1].getDocument_name());

                        appDatabase.subjectsDao().insertCSDocs(cs_docs);
                    }
                    //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().stop();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i("inferno", "MyService getLatestRows response" + error.toString());
                    Toast.makeText(MyService.this, error.toString(), Toast.LENGTH_LONG).show();
                    VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().stop();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> prams = new HashMap<>();
            prams.put("course", course);
            Log.i("inferno", "timestamp2 = " + timestamp);
            prams.put("timestamp", timestamp);
            return prams;
        }
    };
    StringRequest checkForUpdate = new StringRequest(Request.Method.POST, JApplication.getCheckForUpdate(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("inferno", "MyService checkForUpdate response here");
                    Log.i("inferno", "MyService checkForUpdate response" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("response");
                        if (result.equals("upToDate")) {
                        } else if (result.equals("Not Updated")) {
                            getLatestRows.setShouldCache(false);
                            Log.i("inferno", "BEFORE");
                            VolleySingleton.getInstance(getApplicationContext()).addToRequestque(getLatestRows);
                            Log.i("inferno", "AFTER");
                            //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().stop();
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
                    Log.i("inferno", "MyService checkForUpdate error " + error.getMessage());
                    //Toast.makeText(MyService.this, error.toString(), Toast.LENGTH_LONG).show();
                    //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().stop();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> prams = new HashMap<>();
            prams.put("course", course);
            Log.i("inferno", "timestamp1 = " + timestamp);
            prams.put("timestamp", timestamp);
            return prams;
        }
    };
    StringRequest itStringRequest = new StringRequest(Request.Method.POST, JApplication.getSubjectTables(),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("inferno", "MyService itStringRequest " + response);
                    Gson gson = new Gson();
                    IT_Subjects[] itSubjects = gson.fromJson(response, IT_Subjects[].class);
                    appDatabase.subjectsDao().insertITSubjects(itSubjects);
                    //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().stop();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i("inferno", "MyService itStringRequest error" + error.toString());
                    Toast.makeText(MyService.this, error.toString(), Toast.LENGTH_LONG).show();
                    VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().stop();
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
                    Log.i("inferno", "MyService csStringRequest " + response);
                    Gson gson = new Gson();
                    CS_Subjects[] csSubjects = gson.fromJson(response, CS_Subjects[].class);
                    appDatabase.subjectsDao().insertCSSubjects(csSubjects);
                    //VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().stop();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i("inferno", "MyService csStringRequest error" + error.toString());
                    Toast.makeText(MyService.this, "MyService " + error.toString(), Toast.LENGTH_LONG).show();
                    VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().stop();
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> prams = new HashMap<>();
            prams.put("course", "CS");
            return prams;
        }
    };
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    @Override
    public void onCreate() {
        Log.i("inferno", "onCreate");
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        appDatabase = AppDatabase.getAppDatabase(MyService.this);
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);

        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.i("inferno", "onStartCommand");
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job;
        msg = intent.getStringExtra("msg");
        course = intent.getStringExtra("course");

        Message msgg = mServiceHandler.obtainMessage();
        msgg.arg1 = startId;
        if (msg.equals("getSubject")) {
            msgg.arg2 = 22;
        }
        mServiceHandler.sendMessage(msgg);
        Log.i("inferno", msg + course);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    private void downloadSubjects(String course) {
        //        Log.i("inferno", "MyService downloadSubjects");
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();
        if (course.equals("Information Technology")) {
            itStringRequest.setShouldCache(false);
            VolleySingleton.getInstance(getApplicationContext()).addToRequestque(itStringRequest);
            //appDatabase.subjectsDao().insertITSubjects();
        } else if (course.equals("Computer Science")) {
            csStringRequest.setShouldCache(false);
            VolleySingleton.getInstance(getApplicationContext()).addToRequestque(csStringRequest);
            //appDatabase.subjectsDao().insertCSSubjects();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        Log.i("inferno", "onStartCommand");
        return null;
    }

    @Override
    public void onDestroy() {

        Log.i("inferno", "onDestroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    private void checkForUpdates(String course) {
        Log.i("inferno", "MyService checkForUpdates " + course);
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache().clear();
        String temp = new String("null");

        checkForUpdate.setShouldCache(false);

        if (course.equals("Information Technology")) {
            timestamp = String.valueOf(appDatabase.subjectsDao().getITTimestamp());
            Log.i("inferno", "temp = " + temp);
            Log.i("inferno", "appDatabase.subjectsDao().getITTimestamp(); = " + appDatabase.subjectsDao().getITTimestamp());
        } else if (course.equals("Computer Science")) {
            timestamp = String.valueOf(appDatabase.subjectsDao().getCSTimestamp());
            //if(temp.isEmpty())
            Log.i("inferno", "appDatabase.subjectsDao().getCSTimestamp(); = " + appDatabase.subjectsDao().getCSTimestamp());
            Log.i("inferno", "MyService checkForUpdates HERE HERE " + String.valueOf(temp));
        }
//        if (temp.equals("null")) {
//            timestamp = "2000-12-31 23:59:59";
//        } else { timestamp = temp; }
        Log.i("inferno", "MyService checkForUpdates HERE HERE1");
        Log.i("inferno", "timestamp0 = " + timestamp);

        VolleySingleton.getInstance(getApplicationContext()).addToRequestque(checkForUpdate);
        Log.i("inferno", "checkForUpdate called");
        //Toast.makeText(getApplicationContext(), "service runninggg", Toast.LENGTH_SHORT).show();

    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msgg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            Log.i("inferno", "handleMessage");
            Log.i("inferno", "MyService msg = " + msg);
            if (msgg.arg2 == 22) {
                downloadSubjects(course);
                Log.i("inferno", "downloadSubjects called");
            }
            final Handler handler = new Handler();
// Define the code block to be executed
            Runnable runnableCode = new Runnable() {
                @Override
                public void run() {
                    // Do something here on the main thread
                    checkForUpdates(course);
//                    Log.d("Handlers", "Called on main thread");
                    handler.postDelayed(this, 4000);
                }
            };
// Run the above code block on the main thread after 2 seconds
            handler.post(runnableCode);


            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //stopSelf(msg.arg1);
        }
    }

}

