package com.example.dell.student.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.dell.student.Document_Info;

public class StudentViewModel extends AndroidViewModel {

    private StudentRepository mStudentRepository;
    private LiveData<Document_Info[]> mAllDoc;

    public StudentViewModel(@NonNull Application application) {
        super(application);
        mStudentRepository = new StudentRepository(application);
    }

    public LiveData<Document_Info[]> getTheseDoc(String docType, String subName) {
        mAllDoc = mStudentRepository.getTheseDoc(docType, subName);
        return mAllDoc;
    }

}
