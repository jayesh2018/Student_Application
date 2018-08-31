package com.example.dell.student.constants;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.example.dell.student.MyAlarmReceiver;
import com.example.dell.student.MyIntentService;
import com.example.dell.student.R;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by DELL on 15-Mar-18.
 */

public class JApplication {

    public static int i = 2;
    public static int j = 0;
    static String localhost = "192.168.1.102";
    public static String register_url[] = {"http://10.0.8.1:8080/android/Document%20Sharing/registration.php",
            "http://" + localhost + "/android/Document%20Sharing/registration.php",
            "http://onkargujar10.000webhostapp.com/android/Document%20Sharing/registration.php"};
    public static String addUser_url[] = {"http://10.0.8.1:8080/android/Document%20Sharing/userAdd.php",
            "http://" + localhost + "/android/Document%20Sharing/userAdd.php",
            "http://onkargujar10.000webhostapp.com/android/Document%20Sharing/userAdd.php"};
    public static String login_url[] = {"http://10.0.8.1:8080/android/Document%20Sharing/checkStudentLogin.php",
            "http://" + localhost + "/android/Document%20Sharing/checkStudentLogin.php",
            "http://onkargujar10.000webhostapp.com/android/Document%20Sharing/checkStudentLogin.php"};
    public static String subjectTables[] = {"http://10.0.8.1:8080/android/Document%20Sharing/getSubjectTables.php",
            "http://" + localhost + "/android/Document%20Sharing/getSubjectTables.php",
            "http://onkargujar10.000webhostapp.com/android/Document%20Sharing/getSubjectTables.php"};
    public static String uploadDoc[] = {"http://10.0.8.1:8080/android/Document%20Sharing/uploadDoc.php",
            "http://" + localhost + "/android/Document%20Sharing/uploadDoc.php",
            "http://onkargujar10.000webhostapp.com/android/Document%20Sharing/uploadDoc.php"};
    public static String checkForUpdate[] = {"http://10.0.8.1:8080/android/Document%20Sharing/checkForUpdate.php",
            "http://" + localhost + "/android/Document%20Sharing/checkForUpdate.php",
            "http://onkargujar10.000webhostapp.com/android/Document%20Sharing/checkForUpdate.php"};
    public static String getLatestRows[] = {"http://10.0.8.1:8080/android/Document%20Sharing/getLatestRows.php",
            "http://" + localhost + "/android/Document%20Sharing/getLatestRows.php",
            "http://onkargujar10.000webhostapp.com/android/Document%20Sharing/getLatestRows.php"};
    static long enqueue;
    static DownloadManager downloadManager;
    public static BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enqueue);
                Cursor c = downloadManager.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c
                            .getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)) {
                        String column_index = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                        String downloadMimeType = c.getString(c.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                        //Toast.makeText(context,"Download Complete", Toast.LENGTH_LONG).show();
                        Log.i("tagg", " downloaded successfully");
//                        Log.i("tagg","COLUMN_MEDIA_TYPE " + downloadMimeType);
//                        Log.i("tagg","COLUMN_TITLE " + c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE)));
//                        Log.i("tagg","COLUMN_DESCRIPTION " + c.getString(c.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION)));
//                        Log.i("tagg","COLUMN_ID " + c.getString(c.getColumnIndex(DownloadManager.COLUMN_ID)));
                        Intent gIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, gIntent, 0);

                        createNotificationChannel(context);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                                .setSmallIcon(R.drawable.document_sharing)
                                .setContentTitle(column_index)
                                .setContentText("Download Complete")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(i + j++, mBuilder.build());


                    }
                }
            }
            ;
        }

    };

    public static String getRegister_url() {
        return register_url[i];
    }

    public static String getAddUser_url() {
        return addUser_url[i];
    }

    public static String getLogin_url() {
        return login_url[i];
    }

    public static String getSubjectTables() {
        return subjectTables[i];
    }

    public static String getUploadDoc() {
        return uploadDoc[i];
    }

    public static String getCheckForUpdate() {
        return checkForUpdate[i];
    }

    public static String getGetLatestRows() {
        return getLatestRows[i];
    }

    public static void setI(int i) {
        JApplication.i = i;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void logoutClicked(Context context) {
        Log.i("tagg", "on call1");
        String type = "login_details";
        String course;
        String key = "myKey";
        SharedPreferences sharedPreferences = context.getSharedPreferences(type, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key + 1);
        editor.remove(key + 2);
        editor.remove(key + 3);
        editor.commit();
        Intent i = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.stopService(new Intent(context, MyIntentService.class));
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(pIntent);
        context.startActivity(i);
        Log.i("tagg", "on call2");
    }

    public static void addDownloadURL(Context context, Uri downloaduri, String docName) {
        String ext = "." + MimeTypeMap.getFileExtensionFromUrl(downloaduri.getPath());
        downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(downloaduri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle(docName + ext);
        request.setDescription(" . . . . downloading");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/JDocuments/" + docName + ext);


        enqueue = downloadManager.enqueue(request);
    }

    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
