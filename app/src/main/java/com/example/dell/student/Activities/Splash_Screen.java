package com.example.dell.student.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.dell.student.R;

public class Splash_Screen extends AppCompatActivity {

    private static final String type = "login_details";
    private static String key = "myKey";
    private static String username, password, account;
    SharedPreferences sharedPreferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread th1 = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }

        };

        Thread th2 = new Thread() {
            @Override
            public void run() {
                try {
                    sharedPreferences = getSharedPreferences(type, Context.MODE_PRIVATE);
                    username = sharedPreferences.getString(key + 1, "null");
                    password = sharedPreferences.getString(key + 2, "null");
                    account = sharedPreferences.getString(key + 3, "null");
                    if (account.equals("IT")) {
                        intent = new Intent(Splash_Screen.this, Student_Activity.class);
                        intent.putExtra("course", "Information Technology");
                    } else if (account.equals("CS")) {
                        intent = new Intent(Splash_Screen.this, Student_Activity.class);
                        intent.putExtra("course", "Computer Science");
                    } else {
                        intent = new Intent(Splash_Screen.this, Login_Activity.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };

        th1.start();
        th2.start();
    }

}
