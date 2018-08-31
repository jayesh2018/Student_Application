package com.example.dell.student;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.student.constants.JApplication;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class testingService extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 123;
    int requestCode = 12345;
    Intent intent;
    boolean isIntentSafe;
    String Test;
    TextView output;
    String URL = JApplication.getUploadDoc();
    String docPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_service);
        output = findViewById(R.id.textView2);
        intent = new Intent();
        intent.setType("image/*|application/pdf|application/docs");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        requestStoragePermission();
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        isIntentSafe = activities.size() > 0;
        Test = String.valueOf(activities.size());
        Log.i("tagg", String.valueOf(activities.size()));

    }

    public void testClicked(View view) {
        if (isIntentSafe) {
            try {
                startActivityForResult(intent, requestCode);
            } catch (android.content.ActivityNotFoundException ex) {
                // Potentially direct the user to the Market with a Dialog
                Toast.makeText(this, "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == this.requestCode) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            Test += "\nuriString " + uriString;
            Log.i("tagg", "uriString " + uriString);
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            docPath = path;
            Test += "\npath " + path;
            Log.i("tagg", "path " + path);
            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Test += "\ncursor displayName " + displayName;
                        Log.i("tagg", "cursor displayName " + displayName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Test += "\ndisplayName " + displayName;
                Log.i("tagg", "displayName " + displayName);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadClicked(View view) {
//        Log.i("tagg", "docPath" +docPath);
//        output.setText(Test);
//String temp = docPath.substring(2);

        //uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            Log.i("tagg", URL);
            new MultipartUploadRequest(this, uploadId, URL)
                    .addFileToUpload("/storage/emulated/0/Download/learning/onkar.jpg", "doc") //Adding file
                    .addParameter("course", "IT") //Adding text parameter to the request
                    .addParameter("sem", "Sem1")
                    .addParameter("sub_name", "Applied Mathematics I")
                    .addParameter("doc_name", "Jayesh")
                    .addParameter("doc_type", "Notes")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            // your code here
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse,
                                            Exception exception) {
                            // your code here
                            Toast.makeText(testingService.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            // your code here
                            // if you have mapped your server response to a POJO, you can easily get it:
                            // YourClass obj = new Gson().fromJson(serverResponse.getBodyAsString(), YourClass.class);
                            String response = serverResponse.getBodyAsString();
                            Log.i("tagg", "serverResponse.getBodyAsString()" + serverResponse.getBodyAsString());
                            Toast.makeText(getBaseContext(), response.trim(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            // your code here
                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Log.i("tagg", exc.toString());
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Toast.makeText(this, "When can see this", Toast.LENGTH_LONG).show();
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
