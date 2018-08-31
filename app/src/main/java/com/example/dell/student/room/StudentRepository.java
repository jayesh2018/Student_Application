package com.example.dell.student.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.dell.student.Document_Info;

public class StudentRepository {

    private SubjectsDao mSubjectsDao;
    private LiveData<Document_Info[]> mAllDoc;

    StudentRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);
        mSubjectsDao = db.subjectsDao();
    }

    public LiveData<Document_Info[]> getTheseDoc(String docType, String subName) {
        mAllDoc = mSubjectsDao.getTheseDoc(docType, subName);
        return mAllDoc;
    }
}
