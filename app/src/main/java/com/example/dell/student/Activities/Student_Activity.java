package com.example.dell.student.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dell.student.MyAlarmReceiver;
import com.example.dell.student.MyService;
import com.example.dell.student.R;
import com.example.dell.student.YrFragments.OneFragment;
import com.example.dell.student.YrFragments.ThreeFragment;
import com.example.dell.student.YrFragments.TwoFragment;
import com.example.dell.student.constants.JApplication;
import com.example.dell.student.room.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class Student_Activity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 123;
    private static String course;
    Intent intent;
    MyAsyncTask myAsyncTask;
    AppDatabase appDatabase;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = findViewById(R.id.viewpager);
        setupViewpager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setUpToolbar();
        myAsyncTask = new MyAsyncTask();
        intent = new Intent(this, MyService.class);
        if (myAsyncTask.doInBackground() == 0) {
            //intent = new Intent(this, MyIntentService.class);

            intent.putExtra("msg", "getSubject");

        } else {
            intent.putExtra("msg", "checkUpdates");
            Log.i("tagg", "already exists");
        }

        intent.putExtra("course", course);
        startService(intent);
        //scheduleAlarm();

        requestStoragePermission();
    }

    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        intent.putExtra("course", course);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                6 * 10000, pIntent);
    }

    private void setUpToolbar() {
        intent = getIntent();
        course = intent.getStringExtra("course");
        getSupportActionBar().setTitle(course);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                // User chose the "Settings" item, show the app settings UI...
                Log.i("tagg", "before call");
                JApplication.logoutClicked(getBaseContext());
                Log.i("tagg", "after call");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.

                return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menulist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void buttonClicked(int sem) {
        intent = new Intent(this, SubjectsActivity.class);
        intent.putExtra("semNum", sem);
        intent.putExtra("course", course);
        startActivity(intent);
    }

    private void setupViewpager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "F.Y.");
        adapter.addFragment(new TwoFragment(), "S.Y.");
        adapter.addFragment(new ThreeFragment(), "T.Y.");
        viewPager.setAdapter(adapter);
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            int noOfRows;
            appDatabase = AppDatabase.getAppDatabase(Student_Activity.this);
            if (course.equals("Information Technology")) {
                noOfRows = appDatabase.subjectsDao().getITRowsno();
                return noOfRows;
            } else if (course.equals("Computer Science")) {
                noOfRows = appDatabase.subjectsDao().getCSRowsno();
                return noOfRows;
            } else {
                return null;
            }

        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

    }
}
