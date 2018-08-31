package com.example.dell.student.constants;

import android.app.Application;

import com.example.dell.student.BuildConfig;

import net.gotev.uploadservice.UploadService;

/**
 * Created by DELL on 04-Mar-18.
 */

public class Initializer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // setup the broadcast action namespace string which will
        // be used to notify upload status.
        // Gradle automatically generates proper variable as below.

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
    }

}
