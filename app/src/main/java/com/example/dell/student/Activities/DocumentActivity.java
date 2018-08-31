package com.example.dell.student.Activities;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.dell.student.DocFragments.NotesFragment;
import com.example.dell.student.DocFragments.OtherFragment;
import com.example.dell.student.DocFragments.QtnpprFragment;
import com.example.dell.student.R;
import com.example.dell.student.constants.JApplication;

import java.util.ArrayList;
import java.util.List;

public class DocumentActivity extends AppCompatActivity {

    String subjectname, course;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jayesh");

        subjectname = getIntent().getStringExtra("subject");
        course = getIntent().getStringExtra("subject");
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //editText = findViewById(R.id.textView3);
        String temp;
        temp = getIntent().getStringExtra("course");
        temp += getIntent().getStringExtra("subject");
        //editText.setText(temp);

        registerReceiver(JApplication.broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putString("subject", subjectname);
        bundle.putString("course", course);
        NotesFragment notesFragment = new NotesFragment();
        notesFragment.setArguments(bundle);
        QtnpprFragment qtnPprsFragment = new QtnpprFragment();
        qtnPprsFragment.setArguments(bundle);
        OtherFragment otherFragment = new OtherFragment();
        otherFragment.setArguments(bundle);
        adapter.addFragment(notesFragment, "Notes");
        adapter.addFragment(qtnPprsFragment, "Question Papers");
        adapter.addFragment(otherFragment, "Others");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(JApplication.broadcastReceiver);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Nullable
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
