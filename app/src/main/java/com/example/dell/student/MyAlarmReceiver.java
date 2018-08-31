package com.example.dell.student;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";
    Intent intent;
    String course;

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        course = intent.getStringExtra("course");
//        intent = new Intent(context, MyIntentService.class);
        intent = new Intent(context, MyService.class);
        intent.putExtra("course", course);
        intent.putExtra("msg", "checkForUpdates");
        context.startService(intent);
    }
}
