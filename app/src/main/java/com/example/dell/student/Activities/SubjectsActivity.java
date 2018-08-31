package com.example.dell.student.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.dell.student.MyIntentService;
import com.example.dell.student.R;
import com.example.dell.student.RecyclerItemClickListener;
import com.example.dell.student.SubjectAdapter;
import com.example.dell.student.constants.JApplication;
import com.example.dell.student.room.AppDatabase;

public class SubjectsActivity extends AppCompatActivity {

    Intent intent;
    String semSubtitle = "Semester ";
    int semNum = 1;
    String course;
    AppDatabase appDatabase;
    MyAsyncTask myAsyncTask;
    String[] subjects;
    RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.subjectList);

        intent = new Intent(this, MyIntentService.class);
        intent.putExtra("course", course);
        intent.putExtra("msg", "download");
        receiveExtra();
        setUpToolbar();

        myAsyncTask = new MyAsyncTask();
        subjects = myAsyncTask.doInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new SubjectAdapter(subjects));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(SubjectsActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
//                        Log.i("tagg", "onItemClick = " + position);
//                        Log.i("tagg", "subjects = " + course+subjects[position]);
                        intent = new Intent(SubjectsActivity.this, DocumentActivity.class);
                        intent.putExtra("subject", subjects[position]);

                        intent.putExtra("course", course);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
//                        Log.i("tagg", "onLongItemClick = " + position);
                    }
                })
        );
        if (JApplication.isNetworkAvailable(getApplicationContext())) {
            //Log.i("tagg","available");
        } else {
            //Log.i("tagg","not available");
        }
    }


    private void receiveExtra() {
        intent = getIntent();
        semSubtitle += intent.getIntExtra("semNum", 0);
        semNum = intent.getIntExtra("semNum", 0);
        course = intent.getStringExtra("course");
    }

    private void setUpToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Subjects");
        getSupportActionBar().setSubtitle(semSubtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                JApplication.logoutClicked(getApplicationContext());
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menulist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... voids) {
            String sub_sem[];
            appDatabase = AppDatabase.getAppDatabase(SubjectsActivity.this);
            if (course.equals("Information Technology")) {
                sub_sem = appDatabase.subjectsDao().getITSubjects(semNum);
//                Log.i("tagg", "sub_sem[0]" + sub_sem[0]);
                return sub_sem;
            } else if (course.equals("Computer Science")) {
                sub_sem = appDatabase.subjectsDao().getCSSubjects(semNum);
                return sub_sem;
            } else {
                return null;
            }
        }
    }


}
